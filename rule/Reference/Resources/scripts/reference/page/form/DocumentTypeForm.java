package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.DocumentTypeDAO;
import reference.model.DocumentType;

public class DocumentTypeForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			DocumentTypeDAO dao = new DocumentTypeDAO(session);
			DocumentType entity;
			if (!id.isEmpty()) {
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = getDefaultEntity(user, new DocumentType());
				entity.setPrefix("");
			}
			addContent(entity);
			addContent("category", dao.findAllCategories());
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
			entity.setPrefix(formData.getValueSilently("prefix"));
			entity.setLocName(getLocalizedNames(session, formData));

			save(session, entity, dao, isNew);

		} catch (WebFormException | SecureException | DAOException e) {
			logError(e);
			setBadRequest();
		}
	}

	private void save(_Session ses, DocumentType entity, DocumentTypeDAO dao, boolean isNew)
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
		return simpleCheck("name");
	}

	protected DocumentType getDefaultEntity(IUser<Long> user, DocumentType entity) {
		entity.setAuthor(user);
		entity.setName("");
		entity.setCategory("");
		return entity;
	}
}
