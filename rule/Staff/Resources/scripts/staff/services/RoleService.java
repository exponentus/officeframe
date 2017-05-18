package staff.services;

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

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
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

import staff.dao.RoleDAO;
import staff.model.Role;
import staff.ui.Action;

@Path("roles")
public class RoleService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getViewPage() {
		_Session session = getSession();
		IUser<Long> user = session.getUser();
		WebFormData params = getWebFormData();
		int pageSize = session.getPageSize();

		try {
			Outcome outcome = new Outcome();

			SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
			RoleDAO dao = new RoleDAO(session);
			ViewPage<Role> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

			if (user.isSuperUser() || user.getRoles().contains("staff_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				actionBar.addAction(new Action().addNew);
				actionBar.addAction(new Action().deleteDocument);
				outcome.addPayload(actionBar);
			}

			outcome.setTitle("roles");
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
			if (session.getUser().isSuperUser() || session.getUser().getRoles().contains("staff_admin")) {
				actionBar.addAction(new Action().saveAndClose);
			}

			Outcome outcome = new Outcome();
			outcome.setTitle("role");
			outcome.addPayload(entity.getEntityKind(), entity);
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
		IUser<Long> user = session.getUser();

		if (!user.isSuperUser() && !user.getRoles().contains("staff_admin")) {
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
