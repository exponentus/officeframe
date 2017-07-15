package staff.page.action;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;
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
	public void doGET(_Session ses, WebFormData formData) {
		String keyword = formData.getValueSilently("keyword");
		int pageNum = formData.getNumberValueSilently("page", 1);
		int pageSize = ses.getPageSize();
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
