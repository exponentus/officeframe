package reference.page.action;

import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import reference.dao.RequestTypeDAO;
import reference.model.RequestType;

import java.util.List;

public class GetRequestTypesAction extends _DoPage {

    @Override
    public void doGET(_Session session, _WebFormData formData) {
        RequestTypeDAO dao = new RequestTypeDAO(session);
        List<RequestType> list = dao.findAll();
        addContent(new _POJOListWrapper(list, session));
    }
}
