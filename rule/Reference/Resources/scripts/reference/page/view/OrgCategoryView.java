package reference.page.view;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;

import reference.dao.OrgCategoryDAO;

public class OrgCategoryView extends ReferenceView {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		IUser<Long> user = session.getUser();
		try {
			if (user.getId() == SuperUser.ID || user.getRoles().contains("reference_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				_Action newDocAction = new _Action(getLocalizedWord("new_", session.getLang()), "", "new_org_category");
				newDocAction.setURL("p?id=orgcategory-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(new _Action(getLocalizedWord("del_document", session.getLang()), "",
						_ActionType.DELETE_DOCUMENT));
				
				addContent(actionBar);
			}
			addContent(getViewPage(new OrgCategoryDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			
		}
	}

}
