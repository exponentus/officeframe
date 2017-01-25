package staff.page;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.Environment;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.IUser;
import com.exponentus.util.Validator;

import administrator.dao.LanguageDAO;
import administrator.dao.UserDAO;
import administrator.model.User;
import reference.model.Position;
import staff.dao.EmployeeDAO;
import staff.model.Employee;

/**
 * @author Kayra created 05-01-2016
 */

public class UserProfile extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			IUser<Long> user = session.getUser();
			EmployeeDAO dao = new EmployeeDAO(session);
			Employee emp = dao.findByUserId(user.getId());
			_ActionBar actionBar = new _ActionBar(session, getCurrentAppEnv());
			actionBar.addAction(
					new _Action(getLocalizedWord("save_close", session.getLang()), "", _ActionType.SAVE_AND_CLOSE));
			actionBar.addAction(new _Action(getLocalizedWord("close", session.getLang()), "", _ActionType.CLOSE));
			if (emp == null) {
				emp = new Employee();
				User userObj = new User();
				userObj.setUserName(user.getUserName());
				userObj.setLogin(user.getLogin());
				Position tmpPos = new Position();
				tmpPos.setName("");
				emp.setPosition(tmpPos);
				emp.setUser(userObj);
				emp.setName(user.getLogin());
				
			}
			addContent(emp);
			addContent(new LanguageDAO(session).findAllActivated());
			addValue("currentLang", session.getLang().name());
			addValue("pagesize", session.getPageSize());
			addValue("org", Environment.orgName);
			addValue("workspaceUrl", Environment.getWorkspaceURL());
			addContent(actionBar);
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
	}

	@Override
	public void doPOST(_Session session, WebFormData formData) {
		try {
			_Validation ve = validate(formData, session.getLang());
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			IUser<Long> user = session.getUser();
			UserDAO dao = new UserDAO(session);
			IUser<Long> entity = dao.findById(user.getId());

			entity.setLogin(formData.getValue("login"));
			entity.setEmail(formData.getValue("email"));
			if (!formData.getValueSilently("pwd").isEmpty()) {
				entity.setPwd(formData.getValue("pwd"));
			}
			dao.update(entity);

			setRedirect("_back");
		} catch (WebFormException | DAOException e) {
			logError(e);
		}
	}

	private _Validation validate(WebFormData formData, LanguageCode lang) {
		_Validation ve = new _Validation();

		if (formData.getValueSilently("login").isEmpty()) {
			ve.addError("login", "required", getLocalizedWord("required", lang));
		}

		if (!formData.getValueSilently("email").isEmpty()) {
			if (!Validator.checkEmail(formData.getValueSilently("email"))) {
				ve.addError("email", "email", getLocalizedWord("email_invalid", lang));
			}
		}

		if (!formData.getValueSilently("pwd_new").isEmpty()) {
			if (formData.getValueSilently("pwd_confirm").isEmpty()) {
				ve.addError("pwd_confirm", "required", getLocalizedWord("required", lang));
			} else if (!formData.getValueSilently("pwd_new").equals(formData.getValueSilently("pwd_confirm"))) {
				ve.addError("pwd_confirm", "required", getLocalizedWord("password_confirm_not_equals", lang));
			} else if (formData.getValueSilently("pwd_new").length() < 8) {
				ve.addError("pwd_new", "len_ge_8", getLocalizedWord("password_new_invalid", lang));
			}
		}

		return ve;
	}
}
