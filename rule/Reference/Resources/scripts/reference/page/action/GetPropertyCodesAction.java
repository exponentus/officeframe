package reference.page.action;

import kz.lof.scripting._POJOListWrapper;
import kz.lof.scripting._Session;
import kz.lof.scripting._WebFormData;
import kz.lof.scripting.event._DoPage;
import reference.dao.PropertyCodeDAO;
import reference.model.PropertyCode;

import java.util.List;


public class GetPropertyCodesAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        PropertyCodeDAO dao = new PropertyCodeDAO(ses);
        List<PropertyCode> list = dao.findAll();
        addContent(new _POJOListWrapper(list, ses));
    }
}
