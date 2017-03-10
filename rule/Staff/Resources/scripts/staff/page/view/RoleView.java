package staff.page.view;

import java.util.List;
import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;

import staff.dao.RoleDAO;
import staff.model.Employee;
import staff.model.Role;

/**
 * @author Kayra created 08-01-2016
 */

public class RoleView extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		LanguageCode lang = session.getLang();
		IUser<Long> user = session.getUser();
		try {
			RoleDAO dao = new RoleDAO(session);
			String id = formData.getValueSilently("categoryid");
			if (!id.isEmpty()) {
				Role role = dao.findById(UUID.fromString(id));
				List<Employee> emps = role.getEmployees();
				if (user.isSuperUser() || user.getRoles().contains("staff_admin")) {
					_ActionBar actionBar = new _ActionBar(session);
					_Action newDocAction = new _Action(getLocalizedWord("new_", lang), "", "new_employee");
					newDocAction.setURL("Provider?id=employee-form&categoryid=" + id);
					actionBar.addAction(newDocAction);
					actionBar.addAction(
							new _Action(getLocalizedWord("del_document", lang), "", _ActionType.DELETE_DOCUMENT));
					addContent(actionBar);
				}
				addContent(emps);
			} else {
				if (user.getId() == SuperUser.ID || user.getRoles().contains("staff_admin")) {
					_ActionBar actionBar = new _ActionBar(session);
					_Action newDocAction = new _Action(getLocalizedWord("new_", lang), "", "new_role");
					newDocAction.setURL("Provider?id=role-form");
					actionBar.addAction(newDocAction);
					actionBar.addAction(
							new _Action(getLocalizedWord("del_document", lang), "", _ActionType.DELETE_DOCUMENT));
					addContent(actionBar);
				}
				addContent(getViewPage(dao, formData));
			}
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
	}

	@Override
	public void doDELETE(_Session session, WebFormData formData) {
		println(formData);
		try {
			RoleDAO dao = new RoleDAO(session);
			for (String id : formData.getListOfValuesSilently("docid")) {
				Role m = dao.findById(UUID.fromString(id));
				try {
					dao.delete(m);
				} catch (SecureException | DAOException e) {
					setError(e);
				}
			}
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
	}
}
