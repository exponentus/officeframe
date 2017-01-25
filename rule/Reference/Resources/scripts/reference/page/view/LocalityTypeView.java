package reference.page.view;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;

import reference.dao.LocalityTypeDAO;
import reference.model.LocalityType;

public class LocalityTypeView extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		IUser<Long> user = session.getUser();
		try {
			if (user.getId() == SuperUser.ID || user.getRoles().contains("reference_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				_Action newDocAction = new _Action(getLocalizedWord("new_", session.getLang()), "",
						"new_locality_type");
				newDocAction.setURL("p?id=localitytype-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(new _Action(getLocalizedWord("del_document", session.getLang()), "",
						_ActionType.DELETE_DOCUMENT));
				
				addContent(actionBar);
			}
			addContent(getViewPage(new LocalityTypeDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			
		}
	}

	@Override
	public void doDELETE(_Session session, WebFormData formData) {
		try {

			LocalityTypeDAO dao = new LocalityTypeDAO(session);
			for (String id : formData.getListOfValuesSilently("docid")) {
				LocalityType m = dao.findById(UUID.fromString(id));
				dao.delete(m);

			}
		} catch (DAOException | SecureException e) {
			logError(e);
			setBadRequest();
			
		}
	}
}
