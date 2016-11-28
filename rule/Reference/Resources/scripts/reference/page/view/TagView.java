package reference.page.view;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;

import reference.dao.TagDAO;
import reference.model.Tag;

/**
 * @author Kayra created 28-01-2016
 */

public class TagView extends _DoPage {
	
	@Override
	public void doGET(_Session session, _WebFormData formData) {
		IUser<Long> user = session.getUser();
		try {
			if (user.getId() == SuperUser.ID || user.getRoles().contains("reference_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				_Action newDocAction = new _Action(getLocalizedWord("new_", session.getLang()), "", "new_tag");
				newDocAction.setURL("Provider?id=tag-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(new _Action(getLocalizedWord("del_document", session.getLang()), "",
						_ActionType.DELETE_DOCUMENT));

				addContent(actionBar);
			}
			addContent(getViewPage(new TagDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();

		}
	}
	
	@Override
	public void doDELETE(_Session session, _WebFormData formData) {
		try {
			TagDAO dao = new TagDAO(session);
			for (String id : formData.getListOfValuesSilently("docid")) {
				Tag m = dao.findById(UUID.fromString(id));
				
				dao.delete(m);
				
			}
		} catch (DAOException | SecureException e) {
			logError(e);
			setBadRequest();

		}
	}
}
