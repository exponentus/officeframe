package reference.page.action;

import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;

import reference.dao.ReceivingReasonDAO;
import reference.model.ReceivingReason;

public class GetReceivingReasonsAction extends _DoPage {
	
	@Override
	public void doGET(_Session ses, _WebFormData formData) {
		try {
			ReceivingReasonDAO dao = new ReceivingReasonDAO(ses);
			List<ReceivingReason> list = dao.findAll();
			addContent(new _POJOListWrapper(list, ses));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
