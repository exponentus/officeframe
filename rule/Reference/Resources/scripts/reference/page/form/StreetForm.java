package reference.page.form;

import java.util.UUID;

import org.eclipse.persistence.exceptions.DatabaseException;

import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.LocalityDAO;
import reference.dao.StreetDAO;
import reference.model.Locality;
import reference.model.Street;

public class StreetForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		Street entity;
		if (!id.isEmpty()) {
			StreetDAO dao = new StreetDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = (Street) getDefaultEntity(user, new Street());
			LocalityDAO cDao = new LocalityDAO(session);
			Locality city = cDao.findByName("Алматы");
			entity.setLocality(city);
		}
		addContent(entity);
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
			entity.setLocality(localityDAO.findById(formData.getValue("locality")));
			entity.setStreetId(formData.getNumberValueSilently("streetid", 0));
			entity.setLocalizedName(getLocalizedNames(session, formData));

			Street foundEntity = dao.findByName(entity.getName());
			if (foundEntity != null && !foundEntity.equals(entity) && foundEntity.getLocality().equals(entity.getLocality())) {
				ve = new _Validation();
				ve.addError("name", "unique", getLocalizedWord("name_is_not_unique", session.getLang()));
				setBadRequest();
				setValidation(ve);
				return;
			}

			/*
			 * ViewPage<Street> foundEntityList = dao.findAllequal("streetId",
			 * Integer.toString(entity.getStreetId()), 1, 1); if
			 * (foundEntityList.getCount() > 0) { foundEntity =
			 * foundEntityList.getResult().get(0); if (foundEntity != null &&
			 * !foundEntity.equals(entity) &&
			 * foundEntity.getLocality().equals(entity.getLocality())) { ve =
			 * new _Validation(); ve.addError("streetid", "unique",
			 * getLocalizedWord("streetid_is_not_unique", session.getLang()));
			 * setBadRequest(); setValidation(ve); return; } }
			 */

			if (isNew) {
				dao.add(entity);
			} else {
				dao.update(entity);
			}

		} catch (_Exception | DatabaseException | SecureException e) {
			logError(e);
		}
	}

	protected _Validation validate(_WebFormData formData, LanguageCode lang) {
		_Validation ve = simpleCheck("name");

		if (formData.getValueSilently("locality").isEmpty()) {
			ve.addError("locality", "required", getLocalizedWord("field_is_empty", lang));
		}

		return ve;
	}
}
