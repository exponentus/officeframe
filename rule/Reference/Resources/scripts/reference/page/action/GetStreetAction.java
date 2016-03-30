package reference.page.action;

import kz.lof.dataengine.jpa.ViewPage;
import kz.lof.scripting._POJOListWrapper;
import kz.lof.scripting._Session;
import kz.lof.scripting._WebFormData;
import kz.lof.scripting.event._DoPage;
import reference.dao.LocalityDAO;
import reference.dao.StreetDAO;
import reference.model.Locality;
import reference.model.Street;

/**
 * @author Kayra created 02-03-2016
 */

public class GetStreetAction extends _DoPage {

    @Override
    public void doGET(_Session ses, _WebFormData formData) {
        String keyword = formData.getValueSilently("keyword");
        int pageNum = formData.getNumberValueSilently("page", 1);
        int pageSize = ses.pageSize;

        LocalityDAO lDao = new LocalityDAO(ses);
        Locality locality = lDao.findById(formData.getValueSilently("locality"));
        if (locality != null) {
            StreetDAO streetDAO = new StreetDAO(ses);
            ViewPage<Street> streets = streetDAO.findAllInLocalityByKeyword(locality, keyword, pageNum, pageSize);
            addContent(new _POJOListWrapper(streets.getResult(), streets.getMaxPage(), streets.getCount(), streets.getPageNum(), ses));
        } else {
            setValidation(getLocalizedWord("locality_has_not_found", ses.getLang()));
        }
    }
}
