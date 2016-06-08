package reference.page.action;

import java.util.List;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

import reference.dao.LocalityDAO;
import reference.model.Locality;
import reference.model.Region;

/**
 * @author Kayra created 02-03-2016
 */

public class GetLocalityAction extends _DoPage {

	@Override
	public void doGET(_Session ses, _WebFormData formData) {
		int pageNum = formData.getNumberValueSilently("page", 1);
		int pageSize = ses.pageSize;

		LocalityDAO lDao = new LocalityDAO(ses);
		List<Locality> localities = lDao.findAll();

		long count = localities.size();
		int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
		if (pageNum == 0) {
			pageNum = maxPage;
		}
		ViewPage<Region> vp = new ViewPage(localities, count, maxPage, pageNum);
		addContent(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum());

	}
}
