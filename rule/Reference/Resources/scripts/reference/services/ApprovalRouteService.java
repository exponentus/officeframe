package reference.services;

import administrator.dao.LanguageDAO;
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
import reference.dao.ApprovalRouteDAO;
import reference.model.ApprovalRoute;
import reference.model.constants.ApprovalSchemaType;
import reference.model.constants.ApprovalType;
import reference.ui.Action;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static reference.init.AppConst.ROLE_REFERENCE_ADMIN;

@Path("approval_routes")
public class ApprovalRouteService extends RestProvider {

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
            ApprovalRouteDAO dao = new ApprovalRouteDAO(session);
            ViewPage<ApprovalRoute> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

            if (user.isSuperUser() || user.getRoles().contains(ROLE_REFERENCE_ADMIN)) {
                _ActionBar actionBar = new _ActionBar(session);
                actionBar.addAction(new Action().addNew);
                actionBar.addAction(new Action().deleteDocument);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("approval_routes");
            outcome.addPayload("contentTitle", "approval_route");
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
            ApprovalRoute entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new ApprovalRoute();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                ApprovalRouteDAO dao = new ApprovalRouteDAO(session);
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
            outcome.addPayload("contentTitle", "approval_route");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload("languages", new LanguageDAO(session).findAllActivated());
            outcome.addPayload("approvalSchemaType", ApprovalSchemaType.values());
            outcome.addPayload("approvalType", ApprovalType.values());
            outcome.addPayload(actionBar);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(ApprovalRoute dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, ApprovalRoute dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(ApprovalRoute dto) {
        _Session session = getSession();
        IUser user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains(ROLE_REFERENCE_ADMIN)) {
            return null;
        }

        try {
            validate(dto);

            ApprovalRouteDAO dao = new ApprovalRouteDAO(session);
            ApprovalRoute entity;

            if (dto.isNew()) {
                entity = new ApprovalRoute();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setOn(dto.isOn());
            entity.setVersionsSupport(dto.isVersionsSupport());
            entity.setCategory(dto.getCategory());
            entity.setSchema(dto.getSchema());
            entity.setRouteBlocks(dto.getRouteBlocks());
            entity.setLocName(dto.getLocName());
            entity.setLocalizedDescr(dto.getLocalizedDescr());

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
            ApprovalRouteDAO dao = new ApprovalRouteDAO(getSession());
            ApprovalRoute entity = dao.findByIdentefier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(ApprovalRoute entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
