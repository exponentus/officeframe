package staff.page.action;

import java.util.List;
import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

import staff.dao.OrganizationDAO;
import staff.model.Department;
import staff.model.Organization;

public class GetDepartmentsAction extends _DoPage {
	
	@Override
	public void doGET(_Session ses, _WebFormData formData) {
		try {
			String orgId = formData.getValueSilently("organization");
			if (!orgId.isEmpty()) {
				OrganizationDAO orgDao = new OrganizationDAO(ses);
				Organization org = orgDao.findById(UUID.fromString(orgId));
				List<Department> deps = org.getDepartments();
				addContent(new _POJOListWrapper(deps, ses));
			}
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
	}
}
