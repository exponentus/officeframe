package reference.page.action;

import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

import reference.dao.TaskTypeDAO;
import reference.model.TaskType;

public class GetTaskTypesAction extends _DoPage {
	
	@Override
	public void doGET(_Session ses, _WebFormData formData) {
		try {
			TaskTypeDAO dao = new TaskTypeDAO(ses);
			List<TaskType> list = dao.findAll();
			addContent(new _POJOListWrapper(list, ses));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
