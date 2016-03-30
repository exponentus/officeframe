package staff.page.action;


import kz.lof.dataengine.jpa.ViewPage;
import kz.lof.scripting._POJOListWrapper;
import kz.lof.scripting._Session;
import kz.lof.scripting._WebFormData;
import kz.lof.scripting.event._DoPage;
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
