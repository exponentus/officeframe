package reference.page.action;

import kz.lof.scripting._POJOListWrapper;
import kz.lof.scripting._Session;
import kz.lof.scripting._WebFormData;
import kz.lof.scripting.event._DoPage;
import reference.dao.LocalityTypeDAO;
import reference.model.LocalityType;

import java.util.List;


public class GetLocalityTypesAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        LocalityTypeDAO dao = new LocalityTypeDAO(ses);
        List<LocalityType> list = dao.findAll();
        addContent(new _POJOListWrapper(list, ses));
    }
}
