package reference.page.view;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.user.IUser;

import reference.dao.TagDAO;
import reference.model.Tag;

/**
 * @author Kayra created 28-01-2016
 */

public class TagView extends ReferenceView {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		IUser<Long> user = session.getUser();
		try {
			if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
				_ActionBar actionBar = new _ActionBar(session);
				Action newDocAction = new Action(getLocalizedWord("new_", session.getLang()), "", "new_tag");
				newDocAction.setURL("p?id=tag-form");
				actionBar.addAction(newDocAction);
				actionBar.addAction(new Action(getLocalizedWord("del_document", session.getLang()), "",
						_ActionType.DELETE_DOCUMENT));
				addContent(actionBar);
			}

			String category = formData.getValueSilently("category");
			if (category.isEmpty() || (!"software_developing_task".equals(category))
					&& !"software_developing_demand".equals(category)) {
				addContent(getViewPage(new TagDAO(session), formData));
			} else {
				boolean withHidden = formData.getBoolSilently("hidden");
				TagDAO dao = new TagDAO(session);
				ViewPage<Tag> vp = dao.findAllByCategoryAndVisibility(category, withHidden, 1, 0);
				_POJOListWrapper<Tag> plw = new _POJOListWrapper(vp.getResult(), vp.getMaxPage(), vp.getCount(),
						vp.getPageNum(), session);
				addContent(plw);
			}
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}

}
