package integration.page.view;

import java.util.List;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;

import integration.dao.ServiceDAO;
import integration.model.Service;

/**
 * @author Kayra created 18-04-2016
 */

public class ServiceView extends _DoPage {
	
	@Override
	public void doGET(_Session session, WebFormData formData) {
		int pageNum = 1;
		int pageSize = session.pageSize;
		if (formData.containsField("page")) {
			pageNum = formData.getNumberValueSilently("page", pageNum);
		}
		ServiceDAO dao = new ServiceDAO(session);
		long count = dao.getCount();
		int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
		if (pageNum == 0) {
			pageNum = maxPage;
		}
		int startRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
		List<Service> list = dao.findAll(startRec, pageSize);
		addContent(new _POJOListWrapper<Service>(list, maxPage, count, pageNum, session));
		
	}
}
