package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.ControlTypeDAO;
import reference.model.ControlType;

public class ControlTypeForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		ControlTypeDAO dao = new ControlTypeDAO(session);
		ControlType entity;
		if (!id.isEmpty()) {
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = (ControlType) getDefaultEntity(user, new ControlType());
			entity.setDefautltHours(30);
		}
		addContent(entity);
		addContent(new LanguageDAO(session).findAll());
		addContent(getSimpleActionBar(session));
	}

	@Override
	public void doPOST(_Session session, _WebFormData formData) {
		try {
			_Validation ve = simpleCheck("name");
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			ControlTypeDAO dao = new ControlTypeDAO(session);
			ControlType entity;
			String id = formData.getValueSilently("docid");
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new ControlType();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setDefautltHours(formData.getNumberValueSilently("defaulthours", 30));
			entity.setLocalizedName(getLocalizedNames(session, formData));

			save(session, entity, dao, isNew);

		} catch (_Exception | SecureException | DAOException e) {
			logError(e);
		}
	}

	private void save(_Session ses, ControlType entity, ControlTypeDAO dao, boolean isNew) throws SecureException, DAOException {
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

}
