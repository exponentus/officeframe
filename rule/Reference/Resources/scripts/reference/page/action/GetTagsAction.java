package reference.page.action;

import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.event._DoPage;

import reference.dao.TagDAO;
import reference.model.Tag;

public class GetTagsAction extends _DoPage {

	@Override
	public void doGET(_Session ses, WebFormData formData) {
		try {
			TagDAO dao = new TagDAO(ses);
			List<Tag> list = dao.findAll().getResult();
			addContent(new _POJOListWrapper(list, ses));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
