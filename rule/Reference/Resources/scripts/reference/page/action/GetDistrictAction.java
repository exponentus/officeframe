package reference.page.action;

import kz.flabs.runtimeobj.RuntimeObjUtil;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import reference.dao.RegionDAO;
import reference.model.District;
import reference.model.Region;

import java.util.List;

/**
 * @author Kayra created 02-03-2016
 */

public class GetDistrictAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        int pageNum = formData.getNumberValueSilently("page", 1);
        int pageSize = ses.pageSize;

        RegionDAO rDao = new RegionDAO(ses);
        Region region = rDao.findById(formData.getValueSilently("region"));
        if (region != null) {
            List<District> list = region.getDistricts();
            long count = list.size();
            int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
            if (pageNum == 0) {
                pageNum = maxPage;
            }
            ViewPage<Region> vp = new ViewPage(list, count, maxPage, pageNum);
            addContent(new _POJOListWrapper(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum(), ses));
        } else {
            setValidation(getLocalizedWord("region_has_not_found", ses.getLang()));
        }
    }
}
