package dataexport.page.form;

import java.util.UUID;

import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoForm;
import com.exponentus.user.IUser;

import dataexport.dao.ExportProfileDAO;
import dataexport.model.ExportProfile;
import reference.model.constants.CountryCode;

public class ExportProfileForm extends _DoForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		ExportProfile entity;
		if (!id.isEmpty()) {
			ExportProfileDAO dao = new ExportProfileDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = (ExportProfile) getDefaultEntity(user, new ExportProfile());
		}
		addContent(entity);
		addContent(getSimpleActionBar(session));
	}

	@Override
	public void doPOST(_Session session, _WebFormData formData) {
		try {
			_Validation ve = validate(formData, session.getLang());
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			ExportProfileDAO dao = new ExportProfileDAO(session);
			ExportProfile entity;
			String id = formData.getValueSilently("docid");
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new ExportProfile();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));

			entity.setLocalizedName(getLocalizedNames(session, formData));

			save(session, entity, dao, isNew);

		} catch (_Exception | SecureException e) {
			logError(e);
		}
	}

	private void save(_Session ses, ExportProfile entity, ExportProfileDAO dao, boolean isNew) throws SecureException {

		if (isNew) {
			dao.add(entity);
		} else {
			dao.update(entity);
		}
	}

	private _Validation validate(_WebFormData formData, LanguageCode lang) {
		_Validation ve = new _Validation();
		if (formData.getValueSilently("name").isEmpty()) {
			ve.addError("name", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (formData.getValueSilently("code").isEmpty()) {
			ve.addError("code", "required", getLocalizedWord("field_is_empty", lang));
		} else if (formData.getValueSilently("code").equalsIgnoreCase(CountryCode.UNKNOWN.name())) {
			ve.addError("code", "ne_unknown", getLocalizedWord("field_cannot_be_unknown", lang));
		}

		return ve;
	}

}
