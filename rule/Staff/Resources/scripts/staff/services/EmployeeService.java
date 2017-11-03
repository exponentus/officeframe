package staff.services;

import administrator.dao.UserDAO;
import administrator.model.User;
import com.exponentus.common.model.embedded.Avatar;
import com.exponentus.common.service.EntityService;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.actions.ActionBar;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.log.Lg;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._FormAttachments;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import staff.dao.EmployeeDAO;
import staff.dao.RoleDAO;
import staff.dao.filter.EmployeeFilter;
import staff.domain.EmployeeDomain;
import staff.model.Employee;
import staff.model.Role;
import staff.services.helper.RoleProcessor;
import staff.ui.Action;
import staff.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static staff.init.AppConst.ROLE_STAFF_ADMIN;

@Path("employees")
public class EmployeeService extends EntityService<Employee, EmployeeDomain> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser user = session.getUser();
        WebFormData params = getWebFormData();

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            EmployeeFilter filter = setUpFilter(new EmployeeFilter(), params);

            EmployeeDAO dao = new EmployeeDAO(session);
            ViewPage<Employee> vp = dao.findAll(filter, sortParams, params.getPage(),
                    params.getNumberValueSilently("limit", session.getPageSize()));
            ViewOptions viewOptions = new ViewOptions();
            vp.setViewPageOptions(viewOptions.getEmpOptions());
            vp.setFilter(viewOptions.getEmployeeFilter());

            if (user.isSuperUser() || user.getRoles().contains(ROLE_STAFF_ADMIN)) {
                ActionBar actionBar = new ActionBar(session);
                Action action = new Action();
                actionBar.addAction(action.addNew);
                actionBar.addAction(action.deleteDocument);
                actionBar.addAction(action.refreshVew);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("employees");
            outcome.addPayload("contentTitle", "employees");
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @GET
    @Path("{id}/avatar")
    public Response getAvatar(@PathParam("id") String id) {
        return null;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            _Session session = getSession();
            Employee entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Employee();
                entity.setName("");
                entity.setAuthor(session.getUser());

                User tempUser = new User();
                tempUser.setLogin("");
                entity.setUser(tempUser);
            } else {
                EmployeeDAO dao = new EmployeeDAO(session);
                entity = dao.findByIdentifier(id);
            }

            //
            ActionBar actionBar = new ActionBar(session);
            actionBar.addAction(new Action().close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains(ROLE_STAFF_ADMIN)) {
                actionBar.addAction(new Action().saveAndClose);
            }

            Outcome outcome = new Outcome();
            outcome.setTitle("employee");
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "employee");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(actionBar);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @Override
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Employee dto) {
        dto.setId(null);
        return save(dto);
    }

    @Override
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Employee dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    @Override
    public Response saveForm(Employee dto) {
        // TODO Auto-generated method stub
        return null;
    }

    public Response save(Employee dto) {
        _Session session = getSession();
        IUser user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains(ROLE_STAFF_ADMIN)) {
            return null;
        }

        try {
            validate(dto);

            EmployeeDAO dao = new EmployeeDAO(session);
            Employee entity;

            if (dto.isNew()) {
                entity = new Employee();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setIin(dto.getIin());
            entity.setRank(dto.getRank());
            entity.setOrganization(dto.getOrganization());
            entity.setDepartment(dto.getDepartment());
            entity.setPosition(dto.getPosition());
            RoleDAO roleDAO = new RoleDAO(session);
            List<Role> roles = new ArrayList<>();
            for (Role role : dto.getRoles()) {
                roles.add(roleDAO.findById(role.getId()));
            }
            entity.setRoles(roles);
            UserDAO uDao = new UserDAO();
            User lUser = (User) uDao.findByLogin(dto.getUser().getLogin());
            entity.setUser(lUser);

            String fsId = getWebFormData().getFormSesId();
            _FormAttachments formFiles = session.getFormAttachments(fsId);
            Map<String, com.exponentus.rest.stream.TempFile> attsMap = formFiles.getFieldFile("avatar");
            if (attsMap != null && attsMap.size() > 0) {
                com.exponentus.rest.stream.TempFile att = attsMap.values().iterator().next();
                entity.setAvatar((Avatar) att.convertTo(new Avatar()));
            }

            dao.save(entity);


            RoleProcessor roleProcessor = new RoleProcessor(session, entity);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        roleProcessor.checkSupervisorRole();
                    } catch (SecureException | DAOException e) {
                        Lg.exception(e);
                    }
                }
            });
            t.start();


            Outcome outcome = new Outcome();
            outcome.addPayload(entity);

            return Response.ok(outcome).build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        } catch (DTOException e) {
            return responseValidationError(e);
        }
    }

    @Override
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            EmployeeDAO dao = new EmployeeDAO(getSession());
            Employee entity = dao.findByIdentifier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Employee entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (entity.getOrganization() == null) {
            ve.addError("organization", "required", "field_is_empty");
        }

      /*  if (entity.getDepartment() == null) {
            ve.addError("department", "required", "field_is_empty");
        }*/

        if (entity.getPosition() == null) {
            ve.addError("position", "required", "field_is_empty");
        }

        if (entity.getUser() == null || entity.getUser().getLogin().isEmpty()) {
            ve.addError("login", "required", "field_is_empty");
        } else {
            UserDAO uDao = new UserDAO();
            User user = (User) uDao.findByLogin(entity.getUser().getLogin());
            if (user == null) {
                ve.addError("login", "login", "login_has_not_been_found");
            }
        }

        if (ve.hasError()) {
            throw ve;
        }
    }

    private EmployeeFilter setUpFilter(EmployeeFilter filter, WebFormData params) {

        String[] rolesId = params.getListOfValuesSilently("role");
        if (rolesId.length > 0) {
            List<Role> roles = new ArrayList<>();
            for (String rid : rolesId) {
                Role r = new Role();
                r.setId(UUID.fromString(rid));
                roles.add(r);
            }
            if (!roles.isEmpty()) {
                filter.setRoles(roles);
            }
        }

        if (params.containsField("fired")) {
            filter.setWithFired(params.getBoolSilently("fired"));
        } else {
            filter.setWithFired(true);
        }

        String keyword = params.getValueSilently("keyword");
        if (!keyword.isEmpty()) {
            filter.setKeyword(keyword);
        }

        return filter;
    }
}
