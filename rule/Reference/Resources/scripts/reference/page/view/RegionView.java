package reference.page.view;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.IUser;

import reference.dao.RegionDAO;

public class RegionView extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		IUser<Long> user = session.getUser();
		try {
			if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				_Action newDocAction = new _Action(getLocalizedWord("new_", session.getLang()), "", "new_region");
				newDocAction.setURL("p?id=region-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(new _Action(getLocalizedWord("del_document", session.getLang()), "",
						_ActionType.DELETE_DOCUMENT));

				addContent(actionBar);
			}
			addContent(getViewPage(new RegionDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}

}
