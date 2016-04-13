package reference.page.form;

import java.util.UUID;

import org.eclipse.persistence.exceptions.DatabaseException;

import administrator.dao.LanguageDAO;
import kz.lof.exception.SecureException;
import kz.lof.localization.LanguageCode;
import kz.lof.scripting._POJOListWrapper;
import kz.lof.scripting._Session;
import kz.lof.scripting._Validation;
import kz.lof.scripting._WebFormData;
import kz.lof.user.IUser;
import kz.nextbase.script._Exception;
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
		addContent(new _POJOListWrapper(new LanguageDAO(session).findAll(), session));
		addContent(getSimpleActionBar(session));
		startSaveFormTransact(entity);
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

			finishSaveFormTransact(entity);
		} catch (_Exception | DatabaseException | SecureException e) {
			error(e);
		}
	}

	@Override
	protected _Validation validate(_WebFormData formData, LanguageCode lang) {
		_Validation ve = new _Validation();
		if (formData.getValueSilently("name").isEmpty()) {
			ve.addError("name", "required", getLocalizedWord("field_is_empty", lang));
		}

		/*
		 * if (formData.getValueSilently("streetid").isEmpty()) {
		 * ve.addError("streetid", "required",
		 * getLocalizedWord("field_is_empty", lang)); } else if
		 * (formData.getNumberValueSilently("streetid", 0) <= 0) {
		 * ve.addError("streetid", "gt_0",
		 * getLocalizedWord("should_be_contain_value_more_than_zero", lang)); }
		 */

		if (formData.getValueSilently("locality").isEmpty()) {
			ve.addError("locality", "required", getLocalizedWord("field_is_empty", lang));
		}

		return ve;
	}
}
