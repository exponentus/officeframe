package reference.page.action;

import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._POJOListWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.event._DoPage;

import reference.dao.RegionTypeDAO;
import reference.model.RegionType;

public class GetRegionTypesAction extends _DoPage {

	@Override
	public void doGET(_Session ses, WebFormData formData) {
		try {
			RegionTypeDAO dao = new RegionTypeDAO(ses);
			List<RegionType> list = dao.findAll().getResult();
			addContent(new _POJOListWrapper(list, ses));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
