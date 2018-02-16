package staff.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.actions.ActionBar;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import staff.dao.OrganizationLabelDAO;
import staff.model.OrganizationLabel;
import staff.ui.Action;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static staff.init.ModuleConst.ROLE_STAFF_ADMIN;

@Path("organization-labels")
public class OrganizationLabelService extends RestProvider {

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
            OrganizationLabelDAO dao = new OrganizationLabelDAO(session);
            ViewPage<OrganizationLabel> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

            if (user.isSuperUser() || user.getRoles().contains(ROLE_STAFF_ADMIN)) {
                ActionBar actionBar = new ActionBar(session);
                Action action = new Action();
                actionBar.addAction(action.addNew);
                actionBar.addAction(action.deleteDocument);
                actionBar.addAction(action.refreshVew);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("organization_labels");
            outcome.setPayloadTitle("organization_labels");
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
            OrganizationLabel entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new OrganizationLabel();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                OrganizationLabelDAO dao = new OrganizationLabelDAO(session);
                entity = dao.findByIdentifier(id);
            }

            //
            ActionBar actionBar = new ActionBar(session);
            actionBar.addAction(new Action().close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains(ROLE_STAFF_ADMIN)) {
                actionBar.addAction(new Action().saveAndClose);
            }

            Outcome outcome = new Outcome();
            outcome.setTitle("organization_label");
            outcome.setModel(entity);
            outcome.setPayloadTitle("organization_label");
            outcome.setFSID(getWebFormData().getFormSesId());
            outcome.addPayload(actionBar);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(OrganizationLabel dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, OrganizationLabel dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(OrganizationLabel dto) {
        _Session session = getSession();
        IUser user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains(ROLE_STAFF_ADMIN)) {
            return null;
        }

        try {
            validate(dto);

            OrganizationLabelDAO dao = new OrganizationLabelDAO(session);
            OrganizationLabel entity;

            if (dto.isNew()) {
                entity = new OrganizationLabel();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setLocalizedDescr(dto.getLocalizedDescr());

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
            OrganizationLabelDAO dao = new OrganizationLabelDAO(getSession());
            OrganizationLabel entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(OrganizationLabel entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
