package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.DefendantTypeDAO;
import reference.model.DefendantType;

public class DefendantTypeForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			DefendantType entity;
			if (!id.isEmpty()) {
				DefendantTypeDAO dao = new DefendantTypeDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (DefendantType) getDefaultEntity(user, new DefendantType());
			}
			addContent(entity);
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
			_Validation ve = simpleCheck("name");
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			String id = formData.getValueSilently("docid");
			DefendantTypeDAO dao = new DefendantTypeDAO(session);
			DefendantType entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new DefendantType();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValueSilently("name"));
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
		} catch (SecureException | DAOException e) {
			logError(e);
		}
	}
}
