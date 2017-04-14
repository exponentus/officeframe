package reference.page.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting.EnumWrapper;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;
import com.exponentus.util.EnumUtil;

import administrator.dao.LanguageDAO;
import reference.dao.ApprovalRouteDAO;
import reference.model.ApprovalRoute;
import reference.model.constants.ApprovalSchemaType;
import reference.model.constants.ApprovalType;
import reference.model.embedded.RouteBlock;
import staff.dao.EmployeeDAO;
import staff.model.Employee;

public class ApprovalRouteForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			ApprovalRoute entity;
			if (!id.isEmpty()) {
				ApprovalRouteDAO dao = new ApprovalRouteDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = new ApprovalRoute();
				entity.setAuthor(user);
				entity.setName("");
				entity.setSchema(ApprovalSchemaType.REJECT_IF_NO);
				entity.setLocalizedDescr(new HashMap<LanguageCode, String>());
				entity.setLocName(new HashMap<LanguageCode, String>());
				entity.setCategory("");
			}
			addContent(entity);
			addContent(new LanguageDAO(session).findAllActivated());
			addContent(new EnumWrapper(ApprovalSchemaType.class.getEnumConstants()));
			addContent(new EnumWrapper(ApprovalType.class.getEnumConstants()));
			addContent(getSimpleActionBar(session));
		} catch (Exception e) {
			logError(e);
			setBadRequest();
			return;
		}

	}

	@Override
	public void doPOST(_Session session, WebFormData formData) {
		devPrint(formData);
		try {
			_Validation ve = simpleCheck("name");
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			String id = formData.getValueSilently("docid");
			ApprovalRouteDAO dao = new ApprovalRouteDAO(session);
			ApprovalRoute entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new ApprovalRoute();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setLocName(getLocalizedNames(session, formData));
			entity.setSchema(ApprovalSchemaType.valueOf(formData.getValueSilently("schema")));
			entity.setOn(formData.getBoolSilently("ison"));

			int timelimit_list_count = formData.getListOfValues("timelimit").length;
			String[] timelimit_list = formData.getListOfValues("timelimit");
			String[] blocktype_list = formData.getListOfValues("route_block_type");
			String[] approvers_list = formData.getListOfValues("approvers");
			// System.out.println("count = "+ count);
			entity.setCategory(formData.getValue("category"));
			List<RouteBlock> routeBlocks = new ArrayList<RouteBlock>();
			for (int i = 0; i < timelimit_list_count; i++) {
				RouteBlock bl = new RouteBlock();
				bl.setType(EnumUtil.getRndElement(ApprovalType.values()));
				List<Employee> approvers = new ArrayList<Employee>();
				EmployeeDAO employeeDAO = new EmployeeDAO(session);
				Employee i_empl = employeeDAO.findById(UUID.fromString(approvers_list[i]));
				approvers.add(i_empl);
				bl.setApprovers(approvers);
			}

			try {
				if (isNew) {
					dao.add(entity);
				} else {
					dao.update(entity);
				}

			} catch (DAOException e) {
				if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
					ve = new _Validation();
					ve.addError("code", "unique_error", getLocalizedWord("code_is_not_unique", session.getLang()));
					setBadRequest();
					setValidation(ve);
					return;
				} else {
					throw e;
				}
			}
		} catch (WebFormException | SecureException | DAOException e) {
			logError(e);
			setBadRequest();
		}
	}

}
