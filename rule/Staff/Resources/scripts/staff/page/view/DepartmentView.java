package staff.page.view;

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

import staff.dao.DepartmentDAO;
import staff.model.Department;

/**
 * @author Kayra created 03-03-2016
 */

public class DepartmentView extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		LanguageCode lang = session.getLang();
		IUser<Long> user = session.getUser();
		try {
			if (user.isSuperUser() || user.getRoles().contains("staff_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				_Action newDocAction = new _Action(getLocalizedWord("new_", lang), "", "new_department");
				newDocAction.setURL("p?id=department-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(
						new _Action(getLocalizedWord("del_document", lang), "", _ActionType.DELETE_DOCUMENT));
				addContent(actionBar);
			}
			addContent(getViewPage(new DepartmentDAO(session), formData));
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
			DepartmentDAO dao = new DepartmentDAO(session);
			for (String id : formData.getListOfValuesSilently("docid")) {
				Department m = dao.findById(UUID.fromString(id));
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
