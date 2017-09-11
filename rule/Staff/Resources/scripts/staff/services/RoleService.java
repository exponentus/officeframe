package staff.services;

import administrator.dao.ApplicationDAO;
import administrator.model.Application;
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
import reference.init.AppConst;
import staff.dao.RoleDAO;
import staff.model.Role;
import staff.ui.Action;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import static staff.init.AppConst.ROLE_STAFF_ADMIN;

@Path("roles")
public class RoleService extends RestProvider {

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
            RoleDAO dao = new RoleDAO(session);
            ViewPage<Role> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

            if (user.isSuperUser() || user.getRoles().contains(ROLE_STAFF_ADMIN)) {
                _ActionBar actionBar = new _ActionBar(session);
                actionBar.addAction(new Action().addNew);
                actionBar.addAction(new Action().deleteDocument);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("roles");
            outcome.addPayload("contentTitle", "roles");
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
            Role entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Role();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                RoleDAO dao = new RoleDAO(session);
                entity = dao.findByIdentefier(id);
            }

            //
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(new Action().close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains(ROLE_STAFF_ADMIN)) {
                actionBar.addAction(new Action().saveAndClose);
            }

            Set<String> allRoles = new HashSet<String>();
            ApplicationDAO dao = new ApplicationDAO();
            allRoles.addAll(Arrays.asList(AppConst.ROLES));
            List<Application> apps = dao.findAll().getResult();
            for (Application app : apps) {
                if (app.isOn()) {
                    Object rolesObj = app.getAvailableRoles();
                    if (rolesObj != null) {
                        allRoles.addAll(Arrays.asList((String[]) rolesObj));
                    }
                }
            }

            Outcome outcome = new Outcome();
            outcome.setTitle("role");
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "role");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(actionBar);
            outcome.addPayload("roles", allRoles);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Role dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Role dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(Role dto) {
        _Session session = getSession();
        IUser user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains(ROLE_STAFF_ADMIN)) {
            return null;
        }

        try {
            validate(dto);

            RoleDAO dao = new RoleDAO(session);
            Role entity;

            if (dto.isNew()) {
                entity = new Role();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());

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
            RoleDAO dao = new RoleDAO(getSession());
            Role entity = dao.findByIdentefier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Role entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
