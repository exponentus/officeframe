package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.PropertyCodeDAO;
import reference.model.PropertyCode;

public class PropertyCodeForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		try {
			PropertyCode entity;
			if (!id.isEmpty()) {
				PropertyCodeDAO dao = new PropertyCodeDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (PropertyCode) getDefaultEntity(user, new PropertyCode());
			}
			addContent(entity);
			addContent(new LanguageDAO(session).findAllActivated());

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
			PropertyCodeDAO dao = new PropertyCodeDAO(session);
			PropertyCode entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new PropertyCode();
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
					ve.addError("code", "unique_error", getLocalizedWord("code_is_not_unique", session.getLang()));
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
