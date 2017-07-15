package reference.page.action;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;
import reference.dao.CountryDAO;
import reference.model.Country;

/**
 * @author Kayra created 17-02-2016
 */

public class GetCountriesAction extends _DoPage {

	@Override
	public void doGET(_Session ses, WebFormData formData) {
		try {
			String keyword = formData.getValueSilently("keyword");
			int pageNum = formData.getNumberValueSilently("page", 1);
			int pageSize = ses.getPageSize();

			CountryDAO dao = new CountryDAO(ses);
			ViewPage<Country> vp = dao.findAllByKeyword(keyword, pageNum, pageSize);
			addContent(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum());
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
