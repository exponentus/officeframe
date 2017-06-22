package reference.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.user.IUser;

import reference.dao.IndustryTypeDAO;
import reference.domain.IndustryTypeDomain;
import reference.model.IndustryType;
import reference.ui.Action;

@Path("industry-types")
public class IndustryTypeService extends ReferenceService<IndustryType, IndustryTypeDomain> {

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
			IndustryTypeDAO dao = new IndustryTypeDAO(session);
			ViewPage<IndustryType> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

			if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				actionBar.addAction(new Action().addNew);
				actionBar.addAction(new Action().deleteDocument);
				outcome.addPayload(actionBar);
			}

			outcome.setTitle("industry_types");
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
			IndustryType entity;
			boolean isNew = "new".equals(id);

			if (isNew) {
				entity = new IndustryType();
				entity.setName("");
				entity.setAuthor(session.getUser());
			} else {
				IndustryTypeDAO dao = new IndustryTypeDAO(session);
				entity = dao.findByIdentefier(id);
			}

			//
			_ActionBar actionBar = new _ActionBar(session);
			actionBar.addAction(new Action().close);
			if (session.getUser().isSuperUser() || session.getUser().getRoles().contains("reference_admin")) {
				actionBar.addAction(new Action().saveAndClose);
			}

			Outcome outcome = new Outcome();
			outcome.addPayload(entity.getEntityKind(), entity);
			outcome.addPayload("kind", entity.getEntityKind());
			outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
			outcome.addPayload(actionBar);

			return Response.ok(outcome).build();
		} catch (DAOException e) {
			return responseException(e);
		}
	}

	@Override
	public Response saveForm(IndustryType dto) {
		try {
			IndustryTypeDomain omd = new IndustryTypeDomain(getSession());
			IndustryType entity = omd.fillFromDto(dto, new Validation(), getWebFormData().getFormSesId());
			Outcome outcome = omd.getOutcome(omd.save(entity));
			return Response.ok(outcome).build();
		} catch (DTOException e) {
			return responseValidationError(e);
		} catch (DAOException | SecureException e) {
			return responseException(e);
		}
	}

	public class Validation implements IValidation<IndustryType> {

		@Override
		public void check(IndustryType dto) throws DTOException {
			DTOException e = new DTOException();

			if (dto.getName() == null) {
				e.addError("name", "required", "field_is_empty");
			}

			if (e.hasError()) {
				throw e;
			}
		}
	}

}
