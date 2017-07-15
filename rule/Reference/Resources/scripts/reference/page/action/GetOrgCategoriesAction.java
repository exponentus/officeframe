package reference.page.action;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;
import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;


public class GetOrgCategoriesAction extends _DoPage {

	@Override
	public void doGET(_Session ses, WebFormData formData) {
		try {
			String keyword = formData.getValueSilently("keyword");
			int pageNum = formData.getNumberValueSilently("page", 1);
			int pageSize = ses.getPageSize();
			OrgCategoryDAO dao = new OrgCategoryDAO(ses);
			ViewPage<OrgCategory> vp = dao.findAllByKeyword(keyword, pageNum, pageSize);
			addContent(new _POJOListWrapper(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum(), ses));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
