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
import reference.dao.LocalityDAO;
import reference.dao.StreetDAO;
import reference.model.Locality;
import reference.model.Street;

public class StreetForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {

		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		try {
			Street entity;
			if (!id.isEmpty()) {
				StreetDAO dao = new StreetDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (Street) getDefaultEntity(user, new Street());
				LocalityDAO cDao = new LocalityDAO(session);
				Locality city = null;
				try {
					city = cDao.findByName("Алматы");
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				entity.setLocality(city);
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
		devPrint(formData);
		try {
			_Validation ve = validate(formData, session.getLang());
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			String id = formData.getValueSilently("docid");
			StreetDAO dao = new StreetDAO(session);
			LocalityDAO localityDAO = new LocalityDAO(session);
			Street entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new Street();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setLocality(localityDAO.findByIdentefier(formData.getValue("locality")));
			entity.setStreetId(formData.getNumberValueSilently("streetid", 0));
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
		} catch (WebFormException | SecureException | DAOException e) {
			logError(e);
			setBadRequest();
		}
	}

	protected _Validation validate(WebFormData formData, LanguageCode lang) {
		_Validation ve = simpleCheck("name");

		if (formData.getValueSilently("locality").isEmpty()) {
			ve.addError("locality", "required", getLocalizedWord("field_is_empty", lang));
		}

		return ve;
	}
}
