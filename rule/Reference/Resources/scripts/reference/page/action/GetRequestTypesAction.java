package reference.page.action;

import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.event._DoPage;

import reference.dao.RequestTypeDAO;
import reference.model.RequestType;

public class GetRequestTypesAction extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			RequestTypeDAO dao = new RequestTypeDAO(session);
			List<RequestType> list = dao.findAll().getResult();
			addContent(new _POJOListWrapper(list, session));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
