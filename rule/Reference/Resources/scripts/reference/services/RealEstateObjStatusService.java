package reference.services;

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
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.user.IUser;
import reference.dao.RealEstateObjStatusDAO;
import reference.model.RealEstateObjStatus;
import reference.model.constants.RealEstateObjStatusCode;
import reference.ui.Action;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static reference.init.AppConst.ROLE_REFERENCE_ADMIN;

@Path("real_estate_obj_statuses")
public class RealEstateObjStatusService extends RestProvider {

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
            RealEstateObjStatusDAO dao = new RealEstateObjStatusDAO(session);
            ViewPage<RealEstateObjStatus> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

            if (user.isSuperUser() || user.getRoles().contains(ROLE_REFERENCE_ADMIN)) {
                _ActionBar actionBar = new _ActionBar(session);
                actionBar.addAction(new Action().addNew);
                actionBar.addAction(new Action().deleteDocument);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("real_estate_obj_statuses");
            outcome.addPayload("contentTitle", "real_estate_obj_statuses");
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
            RealEstateObjStatus entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new RealEstateObjStatus();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                RealEstateObjStatusDAO dao = new RealEstateObjStatusDAO(session);
                entity = dao.findByIdentefier(id);
            }

            //
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(new Action().close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains(ROLE_REFERENCE_ADMIN)) {
                actionBar.addAction(new Action().saveAndClose);
            }

            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "real_estate_obj_status");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(actionBar);
            outcome.addPayload("realEstateObjStatusCode", RealEstateObjStatusCode.values());

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(RealEstateObjStatus dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, RealEstateObjStatus dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(RealEstateObjStatus dto) {
        _Session session = getSession();
        IUser user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains(ROLE_REFERENCE_ADMIN)) {
            return null;
        }

        try {
            validate(dto);

            RealEstateObjStatusDAO dao = new RealEstateObjStatusDAO(session);
            RealEstateObjStatus entity;

            if (dto.isNew()) {
                entity = new RealEstateObjStatus();
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
        } catch (DTOException e) {
            return responseValidationError(e);
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            RealEstateObjStatusDAO dao = new RealEstateObjStatusDAO(getSession());
            RealEstateObjStatus entity = dao.findByIdentefier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(RealEstateObjStatus entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (entity.getCode() == null) {
            ve.addError("code", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}