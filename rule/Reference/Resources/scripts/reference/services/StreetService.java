package reference.services;

import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.dao.LocalityDAO;
import reference.dao.StreetDAO;
import reference.model.Locality;
import reference.model.Street;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;


@Path("streets")
public class StreetService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser user = session.getUser();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            String localityId = params.getValueSilently("locality");

            StreetDAO streetDAO = new StreetDAO(session);
            ViewPage<Street> vp;

            if (localityId == null || localityId.isEmpty()) {
                vp = streetDAO.findViewPage(sortParams, params.getPage(), pageSize);
            } else {
                LocalityDAO localityDAO = new LocalityDAO(session);
                Locality locality = localityDAO.findByIdentifier(localityId);
                List<Street> streetList = locality.getStreets();
                vp = new ViewPage<Street>(streetList, streetList.size(), 1, 1);
            }
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.setTitle("streets");
            outcome.addPayload("contentTitle", "streets");
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
            Street entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Street();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                StreetDAO dao = new StreetDAO(session);
                entity = dao.findByIdentifier(id);
            }

            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "street");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(getDefaultFormActionBar(entity));

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Street dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Street dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(Street dto) {
        _Session session = getSession();

        try {
            validate(dto);

            StreetDAO dao = new StreetDAO(session);
            Street entity;

            if (dto.isNew()) {
                entity = new Street();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setLocality(dto.getLocality());
            entity.setStreetId(dto.getStreetId());

            dao.save(entity);

            Outcome outcome = new Outcome();
            outcome.addPayload(entity);

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
            StreetDAO dao = new StreetDAO(getSession());
            Street entity = dao.findByIdentifier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Street entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (entity.getLocality() == null) {
            ve.addError("locality", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
