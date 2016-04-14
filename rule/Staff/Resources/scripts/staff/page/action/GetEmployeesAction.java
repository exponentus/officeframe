package staff.page.action;


import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import staff.dao.EmployeeDAO;

/**
 * @author Kayra created 09-01-2016
 */

public class GetEmployeesAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        String keyword = formData.getEncodedValueSilently("keyword");
        int pageNum = formData.getNumberValueSilently("page", 1);
        int pageSize = ses.pageSize;
        EmployeeDAO empDao = new EmployeeDAO(ses);
        ViewPage emps = empDao.findAllByName(keyword, pageNum, pageSize);
        addContent(new _POJOListWrapper(emps.getResult(), emps.getMaxPage(), emps.getCount(), emps.getPageNum(), ses));
    }
}
