package integration.page.view;

import java.util.List;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.rest.ResourceLoader;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

/**
 * @author Kayra created 18-04-2016
 */

public class ServiceView extends _DoPage {

	@Override
	public void doGET(_Session session, _WebFormData formData) {

		int pageNum = formData.getNumberValueSilently("page", 1);
		int pageSize = session.pageSize;
		List<String> services = ResourceLoader.getLoaded();

		long count = services.size();
		int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
		if (pageNum == 0) {
			pageNum = maxPage;
		}

		ViewPage<String> vp = new ViewPage(services, count, maxPage, pageNum);

		// addContent(vp.getResult(), vp.getMaxPage(), vp.getCount(),
		// vp.getPageNum());

	}
}
