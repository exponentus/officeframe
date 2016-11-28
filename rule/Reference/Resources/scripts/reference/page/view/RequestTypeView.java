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

import reference.dao.RequestTypeDAO;
import reference.model.RequestType;

public class RequestTypeView extends _DoPage {
	
	@Override
	public void doGET(_Session session, _WebFormData formData) {
		IUser<Long> user = session.getUser();
		try {
			if (user.getId() == SuperUser.ID || user.getRoles().contains("reference_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				_Action newDocAction = new _Action(getLocalizedWord("new_", session.getLang()), "", "new_request_type");
				newDocAction.setURL("Provider?id=requesttype-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(new _Action(getLocalizedWord("del_document", session.getLang()), "",
						_ActionType.DELETE_DOCUMENT));
				addContent(actionBar);
			}
			addContent(getViewPage(new RequestTypeDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();

		}
	}
	
	@Override
	public void doDELETE(_Session session, _WebFormData formData) {
		try {
			
			RequestTypeDAO dao = new RequestTypeDAO(session);
			for (String id : formData.getListOfValuesSilently("docid")) {
				RequestType m = dao.findById(UUID.fromString(id));
				dao.delete(m);
				
			}
		} catch (DAOException | SecureException e) {
			logError(e);
			setBadRequest();
			
		}
	}
}
