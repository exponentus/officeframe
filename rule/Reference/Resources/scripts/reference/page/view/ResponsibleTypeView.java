package reference.page.view;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions.ActionType;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.IUser;

import reference.dao.ResponsibleTypeDAO;

public class ResponsibleTypeView extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		IUser user = session.getUser();
		try {
			if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				Action newDocAction = new Action(getLocalizedWord("new_", session.getLang()), "",
						"new_responsible_type");
				newDocAction.setURL("p?id=responsibletype-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(new Action(getLocalizedWord("del_document", session.getLang()), "",
						ActionType.DELETE_DOCUMENT));
				addContent(actionBar);
			}
			addContent(getViewPage(new ResponsibleTypeDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();

		}
	}

}
