package reference.page.view;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.IUser;

import reference.dao.RequestTypeDAO;

public class RequestTypeView extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		IUser<Long> user = session.getUser();
		try {
			if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				Action newDocAction = new Action(getLocalizedWord("new_", session.getLang()), "", "new_request_type");
				newDocAction.setURL("p?id=requesttype-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(new Action(getLocalizedWord("del_document", session.getLang()), "",
						_ActionType.DELETE_DOCUMENT));
				addContent(actionBar);
			}
			addContent(getViewPage(new RequestTypeDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();

		}
	}

}
