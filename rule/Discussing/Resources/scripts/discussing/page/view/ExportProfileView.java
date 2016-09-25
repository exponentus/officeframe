package discussing.page.view;

import java.util.UUID;

import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;

import dataexport.dao.ExportProfileDAO;
import dataexport.model.ExportProfile;

public class ExportProfileView extends _DoPage {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		_ActionBar actionBar = new _ActionBar(session);
		_Action newDocAction = new _Action(getLocalizedWord("new_", session.getLang()), "", "new_exportprofile");
		newDocAction.setURL("Provider?id=exportprofile-form");
		actionBar.addAction(newDocAction);
		actionBar.addAction(new _Action(getLocalizedWord("del_document", session.getLang()), "", _ActionType.DELETE_DOCUMENT));
		addContent(actionBar);
		addContent(getViewPage(new ExportProfileDAO(session), formData));
	}

	@Override
	public void doDELETE(_Session session, _WebFormData formData) {
		println(formData);

		ExportProfileDAO dao = new ExportProfileDAO(session);
		for (String id : formData.getListOfValuesSilently("docid")) {
			ExportProfile c = dao.findById(UUID.fromString(id));
			try {
				dao.delete(c);
			} catch (SecureException e) {
				setError(e);
			}
		}
	}
}
