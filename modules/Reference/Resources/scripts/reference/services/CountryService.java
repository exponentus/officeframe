package reference.services;

import administrator.dao.LanguageDAO;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.CountryDAO;
import reference.model.Country;
import reference.model.constants.CountryCode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("countries")
public class CountryService extends ReferenceService<Country> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            CountryDAO dao = new CountryDAO(session);
            ViewPage<Country> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.setTitle("countries");
            outcome.setPayloadTitle("countries");
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
                entity = dao.findById(id);
            }

            Outcome outcome = new Outcome();
            outcome.setModel(entity);
            outcome.setPayloadTitle("country");
            outcome.setFSID(getWebFormData().getFormSesId());
            outcome.addPayload(getDefaultFormActionBar(entity));
            outcome.addPayload("countryCodes", CountryCode.values());
            outcome.addPayload("languages", new LanguageDAO(session).findAllActivated());

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
            outcome.setModel(entity);

            return Response.ok(outcome).build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        } catch (DTOException e) {
            return responseValidationError(e);
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            CountryDAO dao = new CountryDAO(getSession());
            Country entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Country entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (entity.getCode() == null) {
            ve.addError("code", "required", "field_is_empty");
        } else if (entity.getCode().toString().equalsIgnoreCase(CountryCode.UNKNOWN.name())) {
            ve.addError("code", "ne_unknown", "field_cannot_be_unknown");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
