package staff.page.view;

import java.util.UUID;

import javax.persistence.RollbackException;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions.ActionType;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.IUser;

import staff.dao.EmployeeDAO;
import staff.model.Employee;

/**
 * @author Kayra created 07-01-2016
 */

public class EmployeeView extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		LanguageCode lang = session.getLang();
		IUser user = session.getUser();
		try {
			if (user.isSuperUser() || user.getRoles().contains("staff_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				Action newDocAction = new Action(getLocalizedWord("new_", lang), "", "new_employee");
				newDocAction.setURL("p?id=employee-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(
						new Action(getLocalizedWord("del_document", lang), "", ActionType.DELETE_DOCUMENT));
				addContent(actionBar);
			}
			addContent(getViewPage(new EmployeeDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}

	@Override
	public void doDELETE(_Session session, WebFormData formData) {
		try {
			IUser user = session.getUser();
			if (user.isSuperUser() || user.getRoles().contains("staff_admin")) {
				EmployeeDAO dao = new EmployeeDAO(session);
				for (String id : formData.getListOfValuesSilently("docid")) {
					Employee m = dao.findById(UUID.fromString(id));
					if (m != null) {
						try {
							dao.delete(m);
						} catch (RollbackException e) {
							setError(new DAOException(e));
						} catch (SecureException e) {
							setError(e);
						}
					}
				}
			} else {
				setError(new SecureException(getCurrentAppEnv().appName, "deleting_is_restricted", session.getLang()));
			}
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
