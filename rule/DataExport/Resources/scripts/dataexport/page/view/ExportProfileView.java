package dataexport.page.view;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions.ActionType;
import com.exponentus.scripting.event._DoPage;

import dataexport.dao.ExportProfileDAO;
import dataexport.model.ExportProfile;

public class ExportProfileView extends _DoPage {
	
	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			_ActionBar actionBar = new _ActionBar(session);
			Action newDocAction = new Action(getLocalizedWord("new_", session.getLang()), "", "new_exportprofile");
			newDocAction.setURL("p?id=exportprofile-form");
			actionBar.addAction(newDocAction);
			actionBar.addAction(
					new Action(getLocalizedWord("del_document", session.getLang()), "", ActionType.DELETE_DOCUMENT));
			addContent(actionBar);
			addContent(getViewPage(new ExportProfileDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
	
	@Override
	public void doDELETE(_Session session, WebFormData formData) {
		try {
			ExportProfileDAO dao = new ExportProfileDAO(session);
			for (String id : formData.getListOfValuesSilently("docid")) {
				ExportProfile c = dao.findById(UUID.fromString(id));
				try {
					dao.delete(c);
				} catch (SecureException | DAOException e) {
					setError(e);
				}
			}
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
