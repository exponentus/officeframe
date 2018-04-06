package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.AsOfDAO;
import reference.model.AsOf;
import reference.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("as-of")
@Produces(MediaType.APPLICATION_JSON)
public class AsOfService extends ReferenceService<AsOf> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            AsOfDAO dao = new AsOfDAO(session);

            ViewPage<AsOf> vp = dao.findViewPage(sortParams, 0, 0);
            ViewOptions vo = new ViewOptions();
            vp.setViewPageOptions(vo.getAsOfOptions());

            Outcome outcome = new Outcome();
            outcome.setTitle("as_of");
            outcome.setPayloadTitle("as_of");
            outcome.addPayload(getDefaultViewActionBar(true));
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
            AsOf entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new AsOf();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                AsOfDAO dao = new AsOfDAO(session);
                entity = dao.findByIdentifier(id);
            }

            Outcome outcome = new Outcome();
            outcome.setModel(entity);
            outcome.setPayloadTitle("as_of");
            outcome.setFSID(getWebFormData().getFormSesId());
            outcome.addPayload(getDefaultFormActionBar(entity));

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(AsOf dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, AsOf dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(AsOf dto) {
        _Session session = getSession();

        try {
            validate(dto);

            AsOfDAO dao = new AsOfDAO(session);
            AsOf entity;

            if (dto.isNew()) {
                entity = new AsOf();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(getEntityName(dto));
            entity.setLocName(dto.getLocName());
            entity.setAsOfByDate(dto.getAsOfByDate());
            entity.setAllowedToPublish(dto.isAllowedToPublish());

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
            AsOfDAO dao = new AsOfDAO(getSession());
            AsOf entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(AsOf entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getAsOfByDate() == null) {
            ve.addError("asOfByDate", "date", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
