package reference.page.action;

import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
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
