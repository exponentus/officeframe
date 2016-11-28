package reference.page.action;

import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

import reference.dao.DocumentTypeCategoryDAO;
import reference.model.DocumentTypeCategory;

public class GetDocumentTypeCategoryAction extends _DoPage {
	
	@Override
	public void doGET(_Session ses, _WebFormData formData) {
		try {
			DocumentTypeCategoryDAO dao = new DocumentTypeCategoryDAO(ses);
			List<DocumentTypeCategory> list = dao.findAll();
			addContent(new _POJOListWrapper(list, ses));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
