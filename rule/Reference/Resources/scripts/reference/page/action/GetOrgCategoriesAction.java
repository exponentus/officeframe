package reference.page.action;

import kz.lof.dataengine.jpa.ViewPage;
import kz.lof.scripting._POJOListWrapper;
import kz.lof.scripting._Session;
import kz.lof.scripting._WebFormData;
import kz.lof.scripting.event._DoPage;
import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;


public class GetOrgCategoriesAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        String keyword = formData.getValueSilently("keyword");
        int pageNum = formData.getNumberValueSilently("page", 1);
        int pageSize = ses.pageSize;
        OrgCategoryDAO dao = new OrgCategoryDAO(ses);
        ViewPage<OrgCategory> vp = dao.findAllByKeyword(keyword, pageNum, pageSize);
        addContent(new _POJOListWrapper(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum(), ses));
    }
}
