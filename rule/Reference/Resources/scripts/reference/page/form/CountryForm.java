package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._EnumWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.CountryDAO;
import reference.model.Country;
import reference.model.constants.CountryCode;

/**
 * @author Kayra created 03-01-2016
 */

public class CountryForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			Country entity;
			if (!id.isEmpty()) {
				CountryDAO dao = new CountryDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (Country) getDefaultEntity(user, new Country());
			}
			addContent(entity);
			addContent(new _EnumWrapper(CountryCode.class.getEnumConstants()));
			addContent(new LanguageDAO(session).findAllActivated());
			addContent(getSimpleActionBar(session));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
	}

	@Override
	public void doPOST(_Session session, WebFormData formData) {
		try {
			_Validation ve = validate(formData, session.getLang());
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			CountryDAO dao = new CountryDAO(session);
			Country entity;
			String id = formData.getValueSilently("docid");
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new Country();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setCode(CountryCode.valueOf(formData.getValueSilently("code", CountryCode.UNKNOWN.name())));
			entity.setLocName(getLocalizedNames(session, formData));

			save(session, entity, dao, isNew);

		} catch (SecureException | DAOException | WebFormException e) {
			logError(e);
			setBadRequest();
		}
	}

	private void save(_Session ses, Country entity, CountryDAO dao, boolean isNew)
			throws SecureException, DAOException {

		try {
			if (isNew) {
				dao.add(entity);
			} else {
				dao.update(entity);
			}
		} catch (DAOException e) {
			if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
				_Validation ve = new _Validation();
				ve.addError("code", "unique_error", getLocalizedWord("code_is_not_unique", ses.getLang()));
				setBadRequest();
				setValidation(ve);
				return;
			} else {
				throw e;
			}
		}
	}

	protected _Validation validate(WebFormData formData, LanguageCode lang) {
		_Validation ve = simpleCheck("name");

		if (formData.getValueSilently("code").isEmpty()) {
			ve.addError("code", "required", getLocalizedWord("field_is_empty", lang));
		} else if (formData.getValueSilently("code").equalsIgnoreCase(CountryCode.UNKNOWN.name())) {
			ve.addError("code", "ne_unknown", getLocalizedWord("field_cannot_be_unknown", lang));
		}

		return ve;
	}
}
