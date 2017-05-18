package reference.page.action;

import java.util.List;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;

import reference.dao.RegionDAO;
import reference.model.District;
import reference.model.Region;

/**
 * @author Kayra created 02-03-2016
 */

public class GetDistrictAction extends _DoPage {

	@Override
	public void doGET(_Session ses, WebFormData formData) {
		int pageNum = formData.getNumberValueSilently("page", 1);
		int pageSize = ses.getPageSize();
		try {
			RegionDAO rDao = new RegionDAO(ses);
			Region region = rDao.findByIdentefier(formData.getValueSilently("region"));
			if (region != null) {
				List<District> list = region.getDistricts();
				long count = list.size();
				int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
				if (pageNum == 0) {
					pageNum = maxPage;
				}
				ViewPage<District> vp = new ViewPage<District>(list, count, maxPage, pageNum);
				addContent(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum());
			} else {
				setValidation(getLocalizedWord("region_has_not_found", ses.getLang()));
			}
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
