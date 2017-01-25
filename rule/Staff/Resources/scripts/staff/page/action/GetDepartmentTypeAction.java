package staff.page.action;

import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.server.Server;

import reference.dao.DepartmentTypeDAO;
import reference.model.DepartmentType;

public class GetDepartmentTypeAction extends _DoPage {
	
	@Override
	public void doGET(_Session ses, WebFormData formData) {
		try {
			DepartmentTypeDAO dao = new DepartmentTypeDAO(ses);
			List<DepartmentType> list = dao.findAll().getResult();
			addContent(new _POJOListWrapper(list, ses));
		} catch (DAOException e) {
			Server.logger.errorLogEntry(e);
			setBadRequest();
		}
	}
}
