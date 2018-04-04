package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.RealEstateObjPurposeDAO;
import reference.model.RealEstateObjPurpose;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("realestate-purposes")
public class RealEstateObjPurposesService extends ReferenceService<RealEstateObjPurpose> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            RealEstateObjPurposeDAO dao = new RealEstateObjPurposeDAO(session);
            ViewPage<RealEstateObjPurpose> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            Outcome outcome = new Outcome();
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.setTitle("real_estate_obj_purposes");
            outcome.setPayloadTitle("real_estate_obj_purposes");
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
            RealEstateObjPurpose entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new RealEstateObjPurpose();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                RealEstateObjPurposeDAO dao = new RealEstateObjPurposeDAO(session);
                entity = dao.findByIdentifier(id);
            }

            Outcome outcome = new Outcome();
            outcome.setModel(entity);
            outcome.setPayloadTitle("real_estate_obj_purpose");
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
    public Response add(RealEstateObjPurpose dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, RealEstateObjPurpose dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(RealEstateObjPurpose dto) {
        _Session session = getSession();

        try {
            validate(dto);

            RealEstateObjPurposeDAO dao = new RealEstateObjPurposeDAO(session);
            RealEstateObjPurpose entity;

            if (dto.isNew()) {
                entity = new RealEstateObjPurpose();
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
            RealEstateObjPurposeDAO dao = new RealEstateObjPurposeDAO(getSession());
            RealEstateObjPurpose entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(RealEstateObjPurpose entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
