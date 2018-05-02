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
import staff.dao.DepartmentDAO;
import staff.model.Department;
import staff.ui.Action;
import staff.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static staff.init.ModuleConst.ROLE_STAFF_ADMIN;

@Path("departments")
public class DepartmentService extends RestProvider {

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
            DepartmentDAO dao = new DepartmentDAO(session);
            ViewPage<Department> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            vp.setViewPageOptions(new ViewOptions().getDepOptions());

            if (user.isSuperUser() || user.getRoles().contains(ROLE_STAFF_ADMIN)) {
                ActionBar actionBar = new ActionBar(session);
                Action action = new Action();
                actionBar.addAction(action.addNew);
                actionBar.addAction(action.deleteDocument);
                actionBar.addAction(action.refreshVew);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("departments");
            outcome.setPayloadTitle("departments");
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
            Department entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Department();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                DepartmentDAO dao = new DepartmentDAO(session);
                entity = dao.findByIdentifier(id);
            }

            //
            ActionBar actionBar = new ActionBar(session);
            actionBar.addAction(new Action().close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains(ROLE_STAFF_ADMIN)) {
                actionBar.addAction(new Action().saveAndClose);
            }

            Outcome outcome = new Outcome();
            outcome.setTitle("department");
            outcome.setModel(entity);
            outcome.setPayloadTitle("department");
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
    public Response add(Department dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Department dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(Department dto) {
        _Session session = getSession();
        IUser user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains(ROLE_STAFF_ADMIN)) {
            return null;
        }

        try {
            validate(dto);

            DepartmentDAO dao = new DepartmentDAO(session);
            Department entity;

            if (dto.isNew()) {
                entity = new Department();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setRank(dto.getRank());
            entity.setType(dto.getType());
            entity.setOrganization(dto.getOrganization());
            entity.setLeadDepartment(dto.getLeadDepartment());
            entity.setBoss(dto.getBoss());

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
            DepartmentDAO dao = new DepartmentDAO(getSession());
            Department entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Department entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }
        if (entity.getOrganization() == null) {
            ve.addError("organization", "required", "field_is_empty");
        }
        if (entity.getType() == null) {
            ve.addError("type", "required", "field_is_empty");
        }
        if (entity.getId() != null && entity.equals(entity.getLeadDepartment())) {
            ve.addError("leadDepartment", "required", "value_is_not_allowed");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
