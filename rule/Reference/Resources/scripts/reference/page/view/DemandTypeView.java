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

import reference.dao.DemandTypeDAO;
import reference.model.DemandType;

public class DemandTypeView extends _DoPage {
	
	@Override
	public void doGET(_Session session, _WebFormData formData) {
		IUser<Long> user = session.getUser();
		if (user.getId() == SuperUser.ID || user.getRoles().contains("reference_admin")) {
			_ActionBar actionBar = new _ActionBar(session);
			_Action newDocAction = new _Action(getLocalizedWord("new_", session.getLang()), "", "new_demand_type");
			newDocAction.setURL("Provider?id=demandtype-form");
			actionBar.addAction(newDocAction);
			actionBar.addAction(
					new _Action(getLocalizedWord("del_document", session.getLang()), "", _ActionType.DELETE_DOCUMENT));
			addContent(actionBar);
		}
		addContent(getViewPage(new DemandTypeDAO(session), formData));
	}
	
	@Override
	public void doDELETE(_Session session, _WebFormData formData) {
		DemandTypeDAO dao = new DemandTypeDAO(session);
		for (String id : formData.getListOfValuesSilently("docid")) {
			DemandType m = dao.findById(UUID.fromString(id));
			try {
				dao.delete(m);
			} catch (SecureException | DAOException e) {
				setError(e);
			}
		}
	}
}
