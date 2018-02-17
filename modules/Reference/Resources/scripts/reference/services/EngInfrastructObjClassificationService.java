package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.dao.EngInfrastructObjClassificationDAO;
import reference.model.EngInfrastructObjClassification;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("eng-infrastruct-obj-classifications")
public class EngInfrastructObjClassificationService extends ReferenceService<EngInfrastructObjClassification> {

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
            EngInfrastructObjClassificationDAO dao = new EngInfrastructObjClassificationDAO(session);

            ViewPage<EngInfrastructObjClassification> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.setTitle("eng_infrastruct_obj_classifications");
            outcome.setPayloadTitle("eng_infrastruct_obj_classifications");
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
            EngInfrastructObjClassification entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new EngInfrastructObjClassification();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                EngInfrastructObjClassificationDAO dao = new EngInfrastructObjClassificationDAO(session);
                entity = dao.findByIdentifier(id);
            }

            Outcome outcome = new Outcome();
            outcome.setModel(entity);
            outcome.setPayloadTitle("eng_infrastruct_obj_classification");
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
    public Response add(EngInfrastructObjClassification dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, EngInfrastructObjClassification dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(EngInfrastructObjClassification dto) {
        _Session session = getSession();

        try {
            validate(dto);

            EngInfrastructObjClassificationDAO dao = new EngInfrastructObjClassificationDAO(session);
            EngInfrastructObjClassification entity;

            if (dto.isNew()) {
                entity = new EngInfrastructObjClassification();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());

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
            EngInfrastructObjClassificationDAO dao = new EngInfrastructObjClassificationDAO(getSession());
            EngInfrastructObjClassification entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(EngInfrastructObjClassification entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
