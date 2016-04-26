package staff.page.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.exponentus.dataengine.jpa.constants.AppCode;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._Validator;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import administrator.dao.ApplicationDAO;
import administrator.dao.UserDAO;
import administrator.model.Application;
import administrator.model.User;
import reference.dao.PositionDAO;
import reference.model.Position;
import staff.dao.DepartmentDAO;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.dao.RoleDAO;
import staff.model.Employee;
import staff.model.Organization;
import staff.model.Role;

/**
 * @author Kayra created 07-01-2016
 */

public class EmployeeForm extends StaffForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		Employee entity;
		if (!id.isEmpty()) {
			EmployeeDAO dao = new EmployeeDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = new Employee();
			entity.setRegDate(new Date());
			entity.setAuthor(user);
			entity.setName("");
			Organization tmpOrg = new Organization();
			tmpOrg.setName("");
			entity.setOrganization(tmpOrg);
			Position tmpPos = new Position();
			tmpPos.setName("");
			entity.setPosition(tmpPos);
			entity.setRoles(new ArrayList<Role>());
		}
		addContent(entity);
		addContent(getSimpleActionBar(session, session.getLang()));
		addContent(new _POJOListWrapper<>(new RoleDAO(session).findAll(), session));
		startSaveFormTransact(entity);
	}

	@Override
	public void doPOST(_Session session, _WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			EmployeeDAO dao = new EmployeeDAO(session);
			Employee entity;
			boolean isNew = id.isEmpty();

			_Validation ve = validate(formData, session.getLang(), isNew);
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			if (isNew) {
				entity = new Employee();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setIin(formData.getValue("iin"));
			OrganizationDAO orgDAO = new OrganizationDAO(session);
			String orgId = formData.getValueSilently("organization");
			if (!orgId.isEmpty()) {
				Organization org = orgDAO.findById(orgId);
				entity.setOrganization(org);
			}

			String depId = formData.getValueSilently("department");
			if (!depId.isEmpty()) {
				DepartmentDAO depDAO = new DepartmentDAO(session);
				entity.setDepartment(depDAO.findById(depId));
			}

			PositionDAO posDAO = new PositionDAO(session);
			entity.setPosition(posDAO.findById(formData.getValue("position")));
			String[] roles = formData.getListOfValuesSilently("role");
			if (roles != null) {
				RoleDAO roleDAO = new RoleDAO(session);
				List<Role> roleList = new ArrayList<>();
				for (String roleId : roles) {
					if (!roleId.isEmpty()) {
						Role role = roleDAO.findById(roleId);
						roleList.add(role);
					}
				}
				if (!roleList.isEmpty()) {
					entity.setRoles(roleList);
				}
			}

			if (isNew && "on".equals(formData.getValueSilently("reguser"))) {
				String login = formData.getValueSilently("login");
				UserDAO uDao = new UserDAO();
				User user = (User) uDao.findByLogin(login);
				if (user == null) {
					user = new User();
					user.setEmail(formData.getValueSilently("email"));
					user.setLogin(login);
					user.setPwd(formData.getValueSilently("pwd"));
					ApplicationDAO aDao = new ApplicationDAO(session);
					List<AppCode> list = new ArrayList<AppCode>();
					list.add(AppCode.CUSTOM);
					List<Application> appList = aDao.findAllin("code", list, 0, 0).getResult();
					user.setAllowedApps(appList);
					uDao.add(user);
					user = uDao.findById(user.getId());
				}

				entity.setUser(user);
			}

			if (isNew) {
				dao.add(entity);
			} else {
				dao.update(entity);
			}

			finishSaveFormTransact(entity);
		} catch (_Exception e) {
			setBadRequest();
			error(e);
		}
	}

	protected _Validation validate(_WebFormData formData, LanguageCode lang, boolean isNew) {
		_Validation ve = new _Validation();

		if (formData.getValueSilently("name").isEmpty()) {
			ve.addError("name", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (formData.getValueSilently("iin").isEmpty()) {
			ve.addError("iin", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (formData.getValueSilently("organization").isEmpty() && formData.getValueSilently("department").isEmpty()) {
			ve.addError("organization", "required", getLocalizedWord("field_is_empty", lang));
			ve.addError("department", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (formData.getValueSilently("position").isEmpty()) {
			ve.addError("position", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (isNew && "on".equals(formData.getValueSilently("reguser"))) {
			if (formData.getValueSilently("login").isEmpty()) {
				ve.addError("login", "required", getLocalizedWord("field_is_empty", lang));
			}
			if (formData.getValueSilently("email").isEmpty() || !_Validator.checkEmail(formData.getValueSilently("email"))) {
				ve.addError("email", "email", getLocalizedWord("email_invalid", lang));
			}
			if (!formData.getValueSilently("pwd").isEmpty()) {
				if (formData.getValueSilently("pwd_confirm").isEmpty()) {
					ve.addError("pwd_confirm", "required", getLocalizedWord("field_is_empty", lang));
				} else if (!formData.getValueSilently("pwd").equals(formData.getValueSilently("pwd_confirm"))) {
					ve.addError("pwd_confirm", "required", getLocalizedWord("password_confirm_not_equals", lang));
				}
			} else {
				ve.addError("pwd", "required", getLocalizedWord("field_is_empty", lang));
			}
		}

		return ve;
	}
}
