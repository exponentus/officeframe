package staff.page.action;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;

import staff.dao.EmployeeDAO;

/**
 * @author Kayra created 09-01-2016
 */

public class GetEmployeesAction extends _DoPage {

	@Override
	public void doGET(_Session ses, WebFormData formData) {
		try {
			String keyword = formData.getValueSilently("keyword");
			int pageNum = formData.getNumberValueSilently("page", 1);
			int pageSize = ses.pageSize;
			EmployeeDAO empDao = new EmployeeDAO(ses);
			ViewPage emps = empDao.findAllByName(keyword, pageNum, pageSize);
			addContent(emps.getResult(), emps.getMaxPage(), emps.getCount(), emps.getPageNum());
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
	}
}
