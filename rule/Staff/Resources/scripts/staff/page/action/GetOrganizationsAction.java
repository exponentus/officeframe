package staff.page.action;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

import staff.dao.OrganizationDAO;
import staff.model.Organization;

/**
 * @author Kayra created 09-01-2016
 */

public class GetOrganizationsAction extends _DoPage {
	
	@Override
	public void doGET(_Session ses, _WebFormData formData) {
		String keyword = formData.getValueSilently("keyword");
		int pageNum = formData.getNumberValueSilently("page", 1);
		int pageSize = ses.pageSize;
		try {
			OrganizationDAO dao = new OrganizationDAO(ses);
			ViewPage<Organization> vp;
			
			String[] ids = formData.getListOfValuesSilently("ids");
			if (ids.length > 0) {
				List<UUID> uids = new ArrayList<>();
				for (String id : ids) {
					uids.add(UUID.fromString(id));
					
				}
				vp = dao.findAllByIds(uids, 0, 0);
			} else {
				vp = dao.findAllByKeyword(keyword, pageNum, pageSize);
			}
			
			addContent(vp.getResult(), vp.getMaxPage(), vp.getCount(), vp.getPageNum());
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
	}
}
