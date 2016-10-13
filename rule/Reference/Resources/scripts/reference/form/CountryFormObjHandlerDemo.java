package reference.form;

import com.exponentus.rest.incomingpojo.Income;
import com.exponentus.rest.outgoingpojo.IPayload;
import com.exponentus.rest.runtime.HandlerAdapter;
import com.exponentus.rest.runtime.RequestHandler;
import com.exponentus.scripting._Session;

import reference.dao.CountryDAO;
import reference.model.Country;

@RequestHandler("country-form")
public class CountryFormObjHandlerDemo extends HandlerAdapter {

	@Override
	public void doGet(_Session ses, Income request) {
		String docId = request.getDocId();
		Country entity = null;
		if (docId.equals("0")) {
			entity = new Country();
		} else {
			CountryDAO dao = new CountryDAO(ses);
			entity = dao.findById(docId);
		}
		addContent(entity);
	}

	@Override
	public void doPost(_Session ses, Income request) {
		IPayload entity = request.getPayload();
		System.out.println(entity);

	}
}
