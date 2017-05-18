package reference.page.action;

import java.util.List;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;

import reference.dao.CountryDAO;
import reference.model.Country;
import reference.model.Region;
import reference.model.constants.CountryCode;

/**
 * @author Kayra created 17-02-2016
 */

public class GetRegionsAction extends _DoPage {

	@Override
	public void doGET(_Session ses, WebFormData formData) {
		int pageNum = formData.getNumberValueSilently("page", 1);
		int pageSize = ses.getPageSize();
		try {
			CountryDAO cDao = new CountryDAO(ses);
			Country country = cDao.findByCode(CountryCode.KZ);

			if (country != null) {
				List<Region> list = country.getRegions();
				long count = list.size();
				int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
				if (pageNum == 0) {
					pageNum = maxPage;
				}
				ViewPage<Region> vp = new ViewPage(list, count, maxPage, pageNum);
				addContent(new _POJOListWrapper(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum(), ses));
			} else {
				setValidation(getLocalizedWord("country_has_not_found", ses.getLang()));
			}
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
