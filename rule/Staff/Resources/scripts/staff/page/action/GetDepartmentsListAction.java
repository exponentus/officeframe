package staff.page.action;

import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

import staff.dao.DepartmentDAO;
import staff.model.Department;

public class GetDepartmentsListAction extends _DoPage {
	
	@Override
	public void doGET(_Session ses, _WebFormData formData) {
		try {
			DepartmentDAO deptDao = new DepartmentDAO(ses);
			List<Department> deps = deptDao.findAll();
			addContent(new _POJOListWrapper(deps, ses));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
	}
}
