package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.user.IUser;
import reference.dao.CountryDAO;
import reference.model.Country;
import reference.model.constants.CountryCode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("countries")
public class CountryService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser<Long> user = session.getUser();
        WebFormData params = getWebFormData();
        int pageSize = session.pageSize;

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            CountryDAO dao = new CountryDAO(session);
            ViewPage<Country> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

            if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
                _ActionBar actionBar = new _ActionBar(session);
                actionBar.addAction(Action.addNew);
                actionBar.addAction(Action.deleteDocument);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("countries");
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            _Session session = getSession();
            Country entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Country();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                CountryDAO dao = new CountryDAO(session);
                entity = dao.findByIdentefier(id);
            }

            //
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(Action.close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains("reference_admin")) {
                actionBar.addAction(Action.saveAndClose);
            }

            Outcome outcome = new Outcome();
            outcome.addPayload(entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(actionBar);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Country dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Country dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(Country dto) {
        _Session session = getSession();
        IUser<Long> user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains("reference_admin")) {
            return null;
        }

        try {
            validate(dto);

            CountryDAO dao = new CountryDAO(session);
            Country entity;

            if (dto.isNew()) {
                entity = new Country();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setCode(dto.getCode());

            dao.save(entity);

            Outcome outcome = new Outcome();
            outcome.addPayload(entity);

            return Response.ok(outcome).build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        } catch (_Validation.VException e) {
            return responseValidationError(e.getValidation());
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            CountryDAO dao = new CountryDAO(getSession());
            Country entity = dao.findByIdentefier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Country entity) throws _Validation.VException {
        _Validation ve = new _Validation();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (entity.getCode() == null) {
            ve.addError("code", "required", "field_is_empty");
        } else if (entity.getCode().toString().equalsIgnoreCase(CountryCode.UNKNOWN.name())) {
            ve.addError("code", "ne_unknown", "field_cannot_be_unknown");
        }

        ve.assertValid();
    }
}
