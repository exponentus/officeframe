package reference.view;

import com.exponentus.rest.incomingpojo.Income;
import com.exponentus.rest.runtime.HandlerAdapter;
import com.exponentus.rest.runtime.RequestHandler;
import com.exponentus.runtimeobj.SimpleViewObj;
import com.exponentus.scripting._Session;

import reference.dao.CountryDAO;

@RequestHandler("country-view")
public class CountryViewObjHandlerDemo extends HandlerAdapter {

	@Override
	public void doGet(_Session ses, Income income) {
		SimpleViewObj view = income.getPayload(SimpleViewObj.class);
		CountryDAO countryDAO = new CountryDAO(ses);
		addContent(countryDAO.findAll(view.getPageNum(), ses.pageSize));
	}

}
