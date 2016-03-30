package reference.page.action;

import kz.lof.scripting._POJOListWrapper;
import kz.lof.scripting._Session;
import kz.lof.scripting._WebFormData;
import kz.lof.scripting.event._DoPage;
import reference.dao.ReceivingReasonDAO;
import reference.model.ReceivingReason;

import java.util.List;


public class GetReceivingReasonsAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        ReceivingReasonDAO dao = new ReceivingReasonDAO(ses);
        List<ReceivingReason> list = dao.findAll();
        addContent(new _POJOListWrapper(list, ses));
    }
}
