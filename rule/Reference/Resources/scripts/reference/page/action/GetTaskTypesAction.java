package reference.page.action;

import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import reference.dao.TaskTypeDAO;
import reference.model.TaskType;

import java.util.List;


public class GetTaskTypesAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        TaskTypeDAO dao = new TaskTypeDAO(ses);
        List<TaskType> list = dao.findAll();
        addContent(new _POJOListWrapper(list, ses));
    }
}
