package staff.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.exponentus.common.model.embedded.Avatar;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.TempFile;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._FormAttachments;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.user.IUser;

import administrator.dao.UserDAO;
import administrator.model.User;
import staff.dao.EmployeeDAO;
import staff.dao.filter.EmployeeFilter;
import staff.model.Employee;
import staff.model.Role;

@Path("employees")
public class EmployeeService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getViewPage() {
		_Session session = getSession();
		IUser<Long> user = session.getUser();
		WebFormData params = getWebFormData();
		int pageSize = session.pageSize;

		try {
			Outcome outcome = new Outcome();

			SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
			EmployeeFilter filter = setUpFilter(new EmployeeFilter(), params);

			EmployeeDAO dao = new EmployeeDAO(session);
			ViewPage<Employee> vp = dao.findAll(filter, sortParams, params.getPage(), pageSize);

			if (user.isSuperUser() || user.getRoles().contains("staff_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				actionBar.addAction(new _Action("new_", "", "new_"));
				actionBar.addAction(new _Action("del_document", "", _ActionType.DELETE_DOCUMENT));
				outcome.addPayload(actionBar);
			}

			outcome.setTitle("employees");
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
				entity = dao.findById(id);
			}

			//
			_ActionBar actionBar = new _ActionBar(session);
			actionBar.addAction(new _Action("close", "", _ActionType.CLOSE));
			if (session.getUser().isSuperUser() || session.getUser().getRoles().contains("staff_admin")) {
				actionBar.addAction(new _Action("save_close", "", _ActionType.SAVE_AND_CLOSE));
			}

			Outcome outcome = new Outcome();
			outcome.setTitle("employee");
			outcome.addPayload(entity);
			outcome.addPayload("kind", entity.getEntityKind());
			outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
			outcome.addPayload(actionBar);

			return Response.ok(outcome).build();
		} catch (DAOException e) {
			return responseException(e);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Employee dto) {
		dto.setId(null);
		return save(dto);
	}

	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") String id, Employee dto) {
		dto.setId(UUID.fromString(id));
		return save(dto);
	}

	public Response save(Employee dto) {
		_Session session = getSession();
		IUser<Long> user = session.getUser();

		if (!user.isSuperUser() && !user.getRoles().contains("staff_admin")) {
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
			entity.setRoles(dto.getRoles());

			UserDAO uDao = new UserDAO();
			User lUser = (User) uDao.findByLogin(entity.getLogin());
			entity.setUser(lUser);

			String fsId = getWebFormData().getFormSesId();
			_FormAttachments formFiles = session.getFormAttachments(fsId);
			Map<String, TempFile> attsMap = formFiles.getFieldFile("avatar");
			if (attsMap != null && attsMap.size() > 0) {
				TempFile att = attsMap.values().iterator().next();
				entity.setAvatar((Avatar) att.convertTo(new Avatar()));
			}

			dao.save(entity);

			Outcome outcome = new Outcome();
			outcome.addPayload(entity);

			return Response.ok(outcome).build();
		} catch (SecureException | DAOException e) {
			return responseException(e);
		} catch (_Validation.VException e) {
			return responseValidationError(e.getValidation());
		}
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id) {
		try {
			EmployeeDAO dao = new EmployeeDAO(getSession());
			Employee entity = dao.findById(id);
			if (entity != null) {
				dao.delete(entity);
			}
			return Response.noContent().build();
		} catch (SecureException | DAOException e) {
			return responseException(e);
		}
	}

	private void validate(Employee entity) throws _Validation.VException {
		_Validation ve = new _Validation();

		if (entity.getName() == null || entity.getName().isEmpty()) {
			ve.addError("name", "required", "field_is_empty");
		}

		if (entity.getOrganization() == null) {
			ve.addError("organization", "required", "field_is_empty");
		}

		if (entity.getDepartment() == null) {
			ve.addError("department", "required", "field_is_empty");
		}

		if (entity.getPosition() == null) {
			ve.addError("position", "required", "field_is_empty");
		}

		if (entity.getLogin() == null || entity.getLogin().isEmpty()) {
			ve.addError("login", "required", "field_is_empty");
		} else {
			UserDAO uDao = new UserDAO();
			User user = (User) uDao.findByLogin(entity.getLogin());
			if (user == null) {
				ve.addError("login", "login", "login_has_not_been_found");
			}
		}

		ve.assertValid();
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

		return filter;
	}

}
