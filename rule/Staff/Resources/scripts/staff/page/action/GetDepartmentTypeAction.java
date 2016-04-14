package staff.page.action;

import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import reference.dao.DepartmentTypeDAO;
import reference.model.DepartmentType;

import java.util.List;


public class GetDepartmentTypeAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        DepartmentTypeDAO dao = new DepartmentTypeDAO(ses);
        List<DepartmentType> list = dao.findAll();
        addContent(new _POJOListWrapper(list, ses));
    }
}
