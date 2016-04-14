package reference.page.action;

import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import reference.dao.RegionTypeDAO;
import reference.model.RegionType;

import java.util.List;


public class GetRegionTypesAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        RegionTypeDAO dao = new RegionTypeDAO(ses);
        List<RegionType> list = dao.findAll();
        addContent(new _POJOListWrapper(list, ses));
    }
}
