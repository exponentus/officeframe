package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._EnumWrapper;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.DocumentLanguageDAO;
import reference.model.DocumentLanguage;
import reference.model.constants.CountryCode;

public class DocumentLanguageForm extends ReferenceForm {
	
	@Override
	public void doGET(_Session session, _WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			DocumentLanguage entity;
			if (!id.isEmpty()) {
				DocumentLanguageDAO dao = new DocumentLanguageDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (DocumentLanguage) getDefaultEntity(user, new DocumentLanguage());
			}
			addContent(entity);
			addContent(new _EnumWrapper(LanguageCode.class.getEnumConstants()));
			addContent(new LanguageDAO(session).findAll());
			addContent(getSimpleActionBar(session));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
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
			
			DocumentLanguageDAO dao = new DocumentLanguageDAO(session);
			DocumentLanguage entity;
			String id = formData.getValueSilently("docid");
			boolean isNew = id.isEmpty();
			
			if (isNew) {
				entity = new DocumentLanguage();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}
			
			entity.setName(formData.getValue("name"));
			entity.setCode(LanguageCode.valueOf(formData.getValueSilently("code", "UNKNOWN")));
			entity.setLocalizedName(getLocalizedNames(session, formData));
			
			save(session, entity, dao, isNew);
			
		} catch (_Exception | SecureException | DAOException e) {
			logError(e);
		}
	}
	
	private void save(_Session ses, DocumentLanguage entity, DocumentLanguageDAO dao, boolean isNew)
			throws SecureException, DAOException {
		DocumentLanguage foundEntity = dao.findByCode(entity.getCode());
		if (foundEntity != null && !foundEntity.equals(entity)) {
			_Validation ve = new _Validation();
			ve.addError("code", "unique_error", getLocalizedWord("code_is_not_unique", ses.getLang()));
			setBadRequest();
			setValidation(ve);
			return;
		}
		
		if (isNew) {
			dao.add(entity);
		} else {
			dao.update(entity);
		}
	}
	
	protected _Validation validate(_WebFormData formData, LanguageCode lang) {
		_Validation ve = simpleCheck("name");
		
		if (formData.getValueSilently("code").isEmpty()) {
			ve.addError("code", "required", getLocalizedWord("field_is_empty", lang));
		} else if (formData.getValueSilently("code").equalsIgnoreCase(CountryCode.UNKNOWN.name())) {
			ve.addError("code", "ne_unknown", getLocalizedWord("field_cannot_be_unknown", lang));
		}
		
		return ve;
	}
}
