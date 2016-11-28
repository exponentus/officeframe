package discussing.page.view;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;

import discussing.dao.TopicDAO;
import discussing.model.Topic;

public class TopicView extends _DoPage {
	
	@Override
	public void doGET(_Session session, _WebFormData formData) {
		try {
			_ActionBar actionBar = new _ActionBar(session);
			_Action newDocAction = new _Action(getLocalizedWord("new_", session.getLang()), "", "new_topic");
			newDocAction.setURL("p?id=topic-form");
			actionBar.addAction(newDocAction);
			actionBar.addAction(
					new _Action(getLocalizedWord("del_document", session.getLang()), "", _ActionType.DELETE_DOCUMENT));
			addContent(actionBar);

			String[] expandedIds = formData.getListOfValuesSilently("expandedIds");
			List<UUID> expandedIdList = Arrays.stream(expandedIds).map(UUID::fromString).collect(Collectors.toList());
			int pageSize = session.pageSize;
			int pageNum = formData.getNumberValueSilently("page", 0);

			TopicDAO tDao = new TopicDAO(session);
			ViewPage<Topic> vp = tDao.findAllWithChildren(pageNum, pageSize, expandedIdList);
			addContent(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum());
			// addContent(getViewPage(new TopicDAO(session), formData));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
	
	@Override
	public void doDELETE(_Session session, _WebFormData formData) {
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
