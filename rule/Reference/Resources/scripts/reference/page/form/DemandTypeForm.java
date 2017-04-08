package reference.page.form;

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
import reference.dao.DemandTypeDAO;
import reference.model.DemandType;

public class DemandTypeForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			DemandType entity;
			if (!id.isEmpty()) {
				DemandTypeDAO dao = new DemandTypeDAO(session);
				entity = dao.findByIdentefier(id);
			} else {
				entity = (DemandType) getDefaultEntity(user, new DemandType());
				entity.setPrefix("");
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
			_Validation ve = validate(formData, session.getLang());
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			String id = formData.getValueSilently("docid");
			DemandTypeDAO dao = new DemandTypeDAO(session);
			DemandType entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new DemandType();
			} else {
				entity = dao.findByIdentefier(id);
			}

			entity.setName(formData.getValue("name"));
			entity.setPrefix(formData.getValue("prefix"));
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
		}
	}

	protected _Validation validate(WebFormData formData, LanguageCode lang) {
		_Validation ve = simpleCheck("name", "prefix");
		return ve;
	}
}
