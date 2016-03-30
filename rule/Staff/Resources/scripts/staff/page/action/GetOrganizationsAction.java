package staff.page.action;

import kz.lof.dataengine.jpa.ViewPage;
import kz.lof.scripting._POJOListWrapper;
import kz.lof.scripting._Session;
import kz.lof.scripting._WebFormData;
import kz.lof.scripting.event._DoPage;
import staff.dao.OrganizationDAO;
import staff.model.Organization;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Kayra created 09-01-2016
 */

public class GetOrganizationsAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        String keyword = formData.getValueSilently("keyword");
        int pageNum = formData.getNumberValueSilently("page", 1);
        int pageSize = ses.pageSize;
        OrganizationDAO dao = new OrganizationDAO(ses);
        ViewPage<Organization> vp;

        String[] ids = formData.getListOfValuesSilently("ids");
        if (ids.length > 0) {
            List<UUID> uids = new ArrayList<>();
            for (String id : ids) {
                uids.add(UUID.fromString(id));
            }
            vp = dao.findAllByIds(uids, pageNum, pageSize);
        } else {
            vp = dao.findAllByKeyword(keyword, pageNum, pageSize);
        }

        addContent(new _POJOListWrapper(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum(), ses));
    }
}
