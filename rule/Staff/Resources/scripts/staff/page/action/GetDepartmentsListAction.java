package staff.page.action;

import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import staff.dao.DepartmentDAO;
import staff.dao.OrganizationDAO;
import staff.model.Department;
import staff.model.Organization;

import java.util.List;
import java.util.UUID;

public class GetDepartmentsListAction extends _DoPage {

	@Override
	public void doGET(_Session ses, _WebFormData formData) {
		DepartmentDAO deptDao = new DepartmentDAO(ses);
		List<Department> deps = deptDao.findAll();
		addContent(new _POJOListWrapper(deps, ses));
	}
}
