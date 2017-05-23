package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.EnumWrapper;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.ControlTypeDAO;
import reference.model.ControlType;
import reference.model.constants.ControlSchemaType;

public class ControlTypeForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			ControlTypeDAO dao = new ControlTypeDAO(session);
			ControlType entity;
			if (!id.isEmpty()) {
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (ControlType) getDefaultEntity(user, new ControlType());
				entity.setDefaultHours(30);
			}
			addContent(entity);
			addContent(new LanguageDAO(session).findAllActivated());
			addContent(new EnumWrapper(ControlSchemaType.class.getEnumConstants()));
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

			entity.setName(formData.getValueSilently("name"));
			entity.setDefaultHours(formData.getNumberValueSilently("defaulthours", 30));
			entity.setLocName(getLocalizedNames(session, formData));

			save(session, entity, dao, isNew);

		} catch (SecureException | DAOException e) {
			logError(e);
		}
	}

	private void save(_Session ses, ControlType entity, ControlTypeDAO dao, boolean isNew)
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

}
