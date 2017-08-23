package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.PositionDAO;
import reference.model.Position;

public class PositionForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser user = session.getUser();
		try {
			Position entity;
			if (!id.isEmpty()) {
				PositionDAO dao = new PositionDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (Position) getDefaultEntity(user, new Position());
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
			_Validation ve = simpleCheck("name");
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			String id = formData.getValueSilently("docid");
			PositionDAO dao = new PositionDAO(session);
			Position entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new Position();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setRank(formData.getNumberValueSilently("rank", 0));
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
		} catch (WebFormException | SecureException | DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
