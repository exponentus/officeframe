package reference.page.form;

import java.util.UUID;

import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.DocumentTypeDAO;
import reference.model.DocumentType;

public class DocumentTypeForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		DocumentTypeDAO dao = new DocumentTypeDAO(session);
		DocumentType entity;
		if (!id.isEmpty()) {
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = getDefaultEntity(user, new DocumentType());
		}
		addContent(entity);
		addContent("category", dao.findAllCategories());
		addContent(new LanguageDAO(session).findAll());
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

			DocumentTypeDAO dao = new DocumentTypeDAO(session);
			DocumentType entity;
			String id = formData.getValueSilently("docid");
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new DocumentType();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setCategory(formData.getValueSilently("category"));
			entity.setLocalizedName(getLocalizedNames(session, formData));

			save(session, entity, dao, isNew);

		} catch (_Exception | SecureException e) {
			logError(e);
		}
	}

	private void save(_Session ses, DocumentType entity, DocumentTypeDAO dao, boolean isNew) throws SecureException {
		/*
		 * DocumentLanguage foundEntity = dao.findByCode(entity.getCode()); if
		 * (foundEntity != null && !foundEntity.equals(entity)) { _Validation ve
		 * = new _Validation(); ve.addError("code", "unique_error",
		 * getLocalizedWord("code_is_not_unique", ses.getLang()));
		 * setBadRequest(); setValidation(ve); return; }
		 */

		if (isNew) {
			dao.add(entity);
		} else {
			dao.update(entity);
		}
	}

	protected _Validation validate(_WebFormData formData, LanguageCode lang) {
		return simpleCheck("name", "category");
	}

	protected DocumentType getDefaultEntity(IUser<Long> user, DocumentType entity) {
		entity.setAuthor(user);
		entity.setName("");
		entity.setCategory("");
		return entity;
	}
}
