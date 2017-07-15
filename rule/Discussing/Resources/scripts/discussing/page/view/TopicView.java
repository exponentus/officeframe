package discussing.page.view;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions.ActionType;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.event._DoPage;
import discussing.dao.TopicDAO;
import discussing.model.Topic;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TopicView extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			_ActionBar actionBar = new _ActionBar(session);
			Action newDocAction = new Action(getLocalizedWord("new_", session.getLang()), "", "new_topic");
			newDocAction.setURL("p?id=topic-form");
			actionBar.addAction(newDocAction);
			actionBar.addAction(
					new Action(getLocalizedWord("del_document", session.getLang()), "", ActionType.DELETE_DOCUMENT));
			addContent(actionBar);

			String[] expandedIds = formData.getListOfValuesSilently("expandedIds");
			List<UUID> expandedIdList = Arrays.stream(expandedIds).map(UUID::fromString).collect(Collectors.toList());
			int pageSize = session.getPageSize();
			int pageNum = formData.getNumberValueSilently("page", 0);

			TopicDAO tDao = new TopicDAO(session);
			ViewPage<Topic> vp = tDao.findAll(pageNum, pageSize);
			addContent(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum());
			// addContent(getViewPage(new TopicDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}

	@Override
	public void doDELETE(_Session session, WebFormData formData) {
		try {

			TopicDAO dao = new TopicDAO(session);
			for (String id : formData.getListOfValuesSilently("docid")) {
				Topic c = dao.findById(UUID.fromString(id));
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
