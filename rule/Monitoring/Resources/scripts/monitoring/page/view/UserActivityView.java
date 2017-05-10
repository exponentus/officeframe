package monitoring.page.view;

import java.util.List;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.scripting.IPOJOObject;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoPage;

import monitoring.dao.UserActivityDAO;
import monitoring.model.UserActivity;

public class UserActivityView extends _DoPage {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doGET(_Session session, WebFormData formData) {
		_ActionBar actionBar = new _ActionBar(session);
		Action newDocAction = new Action(getLocalizedWord("new_", session.getLang()), "", "new_userprofile");
		newDocAction.setURL("Provider?id=useractivity-form");
		actionBar.addAction(newDocAction);
		actionBar.addAction(new Action(getLocalizedWord("del_document", session.getLang()), "", _ActionType.DELETE_DOCUMENT));
		addContent(actionBar);

		UserActivityDAO dao = new UserActivityDAO(session);
		int pageNum = 1;
		int pageSize = session.pageSize;
		if (formData.containsField("page")) {
			pageNum = formData.getNumberValueSilently("page", pageNum);
		}
		long count = dao.getCount();
		int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
		if (pageNum == 0) {
			pageNum = maxPage;
		}
		int startRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
		List<? extends IPOJOObject> list = dao.findAll(startRec, pageSize);
		addContent(new _POJOListWrapper(list, maxPage, count, pageNum, session));
	}

	@Override
	public void doDELETE(_Session session, WebFormData formData) {
		UserActivityDAO dao = new UserActivityDAO(session);
		for (String id : formData.getListOfValuesSilently("docid")) {
			UserActivity m = dao.findById(Long.parseLong(id));
			dao.delete(m);
		}

	}
}
