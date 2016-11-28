package reference.form;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.incomingpojo.Income;
import com.exponentus.rest.runtime.HandlerAdapter;
import com.exponentus.rest.runtime.RequestHandler;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;

import reference.dao.CountryDAO;
import reference.model.Country;

@RequestHandler("country-form")
public class CountryFormObjHandlerDemo extends HandlerAdapter {

	@Override
	public void doGet(_Session ses, Income request) {
		try {
			String docId = request.getDocId();
			Country entity = null;
			if (docId.equals("0")) {
				entity = new Country();
			} else {
				CountryDAO dao = new CountryDAO(ses);
				entity = dao.findById(docId);
			}
			addContent(entity);
		} catch (DAOException e) {
			Server.logger.errorLogEntry(e);
		}
	}

	@Override
	public void doPost(_Session ses, Income request) {
		Object entity = request.getPayload();
		System.out.println(entity);

	}
}
