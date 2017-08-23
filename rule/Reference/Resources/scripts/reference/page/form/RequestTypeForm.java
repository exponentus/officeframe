package reference.page.form;

import administrator.dao.LanguageDAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;
import reference.dao.RequestTypeDAO;
import reference.model.RequestType;

import java.util.UUID;

public class RequestTypeForm extends ReferenceForm {
	
	@Override
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser user = session.getUser();
		try {
			RequestType entity;
			if (!id.isEmpty()) {
				RequestTypeDAO dao = new RequestTypeDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (RequestType) getDefaultEntity(user, new RequestType());
			}
			addContent(entity);
			
			addContent(new LanguageDAO(session).findAllActivated());

			addContent(getSimpleActionBar(session));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();

		}
	}
	
	@Override
	public void doPOST(_Session session, WebFormData formData) {
		try {
			_Validation ve = simpleCheck("name");
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}
			
			String id = formData.getValueSilently("docid");
			RequestTypeDAO dao = new RequestTypeDAO(session);
			RequestType entity;
			boolean isNew = id.isEmpty();
			
			if (isNew) {
				entity = new RequestType();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}
			
			entity.setName(formData.getValue("name"));
			entity.setLocName(getLocalizedNames(session, formData));
			
			try {
				if (isNew) {
					dao.add(entity);
				} else {
					dao.update(entity);
				}
				
			} catch (DAOException e) {
				if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
					ve = new _Validation();
					ve.addError("name", "unique_error", getLocalizedWord("name_is_not_unique", session.getLang()));
					setBadRequest();
					setValidation(ve);
					return;
				} else {
					throw e;
				}
			}
			
		} catch (WebFormException | SecureException | DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
