package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.DocumentSubjectDAO;
import reference.model.DocumentSubject;

public class DocumentSubjectForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		DocumentSubjectDAO dao = new DocumentSubjectDAO(session);
		DocumentSubject entity;
		if (!id.isEmpty()) {
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = getDefaultEntity(user, new DocumentSubject());
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

			DocumentSubjectDAO dao = new DocumentSubjectDAO(session);
			DocumentSubject entity;
			String id = formData.getValueSilently("docid");
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new DocumentSubject();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setCategory(formData.getValueSilently("category"));
			entity.setLocalizedName(getLocalizedNames(session, formData));

			save(session, entity, dao, isNew);

		} catch (_Exception | SecureException | DAOException e) {
			logError(e);
		}
	}

	private void save(_Session ses, DocumentSubject entity, DocumentSubjectDAO dao, boolean isNew) throws SecureException, DAOException {
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

	protected DocumentSubject getDefaultEntity(IUser<Long> user, DocumentSubject entity) {
		entity.setAuthor(user);
		entity.setName("");
		entity.setCategory("");
		return entity;
	}
}