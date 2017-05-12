package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.TaskTypeDAO;
import reference.model.TaskType;

public class TaskTypeForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		try {
			TaskType entity;
			if (!id.isEmpty()) {
				TaskTypeDAO dao = new TaskTypeDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (TaskType) getDefaultEntity(user, new TaskType());
				entity.setPrefix("");
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
			_Validation ve = validate(formData, session.getLang());
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			String id = formData.getValueSilently("docid");
			TaskTypeDAO dao = new TaskTypeDAO(session);
			TaskType entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new TaskType();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValueSilently("name"));
			entity.setPrefix(formData.getValueSilently("prefix"));
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
		} catch (SecureException | DAOException e) {
			logError(e);
			setBadRequest();
		}
	}

	protected _Validation validate(WebFormData formData, LanguageCode lang) {
		_Validation ve = simpleCheck("name");

		if (formData.getValueSilently("prefix").isEmpty()) {
			ve.addError("prefix", "required", getLocalizedWord("field_is_empty", lang));
		}

		return ve;
	}
}
