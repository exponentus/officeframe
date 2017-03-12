package staff.page.view;

import java.util.List;
import java.util.UUID;

import com.exponentus.common.dao.ViewEntryDAO;
import com.exponentus.common.model.ViewEntry;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.IUser;

import staff.dao.OrganizationDAO;
import staff.model.Organization;

public class StructureView extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		LanguageCode lang = session.getLang();
		try {
			IUser<Long> user = session.getUser();
			if (user.isSuperUser() || user.getRoles().contains("staff_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				_Action newDocAction = new _Action(getLocalizedWord("new_", lang), "", "new_organization");
				newDocAction.setURL("p?id=organization-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(
						new _Action(getLocalizedWord("del_document", lang), "", _ActionType.DELETE_DOCUMENT));
				addContent(actionBar);
			}

			ViewEntryDAO veDao = new ViewEntryDAO(session);
			OrganizationDAO dao = new OrganizationDAO(session);
			Organization org = dao.findPrimaryOrg().get(0);
			if (org != null) {
				// addContent(org);
				List<ViewEntry> descendants = veDao.findAllDescendants(org.getIdentifier());
				addContent(new _POJOListWrapper<>(descendants, session));
			} else {
				addContent(new _POJOListWrapper<>(getLocalizedWord("no_primary_org", lang), ""));
			}
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}

	}

	@Override
	public void doDELETE(_Session session, WebFormData formData) {
		IUser<Long> user = session.getUser();
		if (user.isSuperUser() || user.getRoles().contains("staff_admin")) {
			try {
				OrganizationDAO dao = new OrganizationDAO(session);
				for (String id : formData.getListOfValuesSilently("docid")) {
					Organization m = dao.findById(UUID.fromString(id));
					try {
						dao.delete(m);
					} catch (SecureException | DAOException e) {
						setError(e);
					}
				}
			} catch (DAOException e) {
				logError(e);
				setBadRequest();
				return;
			}
		} else {
			setError(new SecureException(getCurrentAppEnv().appName, "deleting_is_restricted", session.getLang()));
		}
	}

}
