package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.dao.LocalityTypeDAO;
import reference.model.LocalityType;
import reference.model.constants.LocalityCode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("locality-types")
public class LocalityTypeService extends ReferenceService<LocalityType> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser user = session.getUser();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            LocalityTypeDAO dao = new LocalityTypeDAO(session);
            ViewPage<LocalityType> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

            Outcome outcome = new Outcome();
            outcome.setTitle("locality_types");
            outcome.setPayloadTitle("locality_types");
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
            LocalityType entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new LocalityType();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                LocalityTypeDAO dao = new LocalityTypeDAO(session);
                entity = dao.findByIdentifier(id);
            }

            Outcome outcome = new Outcome();
            outcome.setModel(entity);
            outcome.setPayloadTitle("locality_type");
            outcome.addPayload("localityCodes", LocalityCode.values());
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
    public Response add(LocalityType dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, LocalityType dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(LocalityType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            LocalityTypeDAO dao = new LocalityTypeDAO(session);
            LocalityType entity;

            if (dto.isNew()) {
                entity = new LocalityType();
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
            LocalityTypeDAO dao = new LocalityTypeDAO(getSession());
            LocalityType entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(LocalityType entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}