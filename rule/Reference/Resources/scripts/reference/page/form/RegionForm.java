package reference.page.form;

import java.util.Date;
import java.util.UUID;

import kz.lof.administrator.dao.LanguageDAO;
import kz.lof.exception.SecureException;
import kz.lof.localization.LanguageCode;
import kz.lof.scripting._POJOListWrapper;
import kz.lof.scripting._Session;
import kz.lof.scripting._Validation;
import kz.lof.scripting._WebFormData;
import kz.lof.user.IUser;
import kz.nextbase.script._Exception;

import org.eclipse.persistence.exceptions.DatabaseException;

import reference.dao.CountryDAO;
import reference.dao.RegionDAO;
import reference.dao.RegionTypeDAO;
import reference.model.Country;
import reference.model.Region;
import reference.model.RegionType;
import reference.model.constants.CountryCode;
import reference.model.constants.RegionCode;

/**
 * @author Kayra created 03-01-2016
 */

public class RegionForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		RegionTypeDAO regionTypeDAO = new RegionTypeDAO(session);
		Region entity;
		if (!id.isEmpty()) {
			RegionDAO dao = new RegionDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			CountryDAO cDao = new CountryDAO(session);
			entity = new Region();
			entity.setAuthor(user);
			entity.setRegDate(new Date());
			entity.setName("");
			RegionType regionType = regionTypeDAO.findByCode(RegionCode.REGION);
			entity.setType(regionType);
			Country country = cDao.findByCode(CountryCode.KZ);
			if (country != null) {
				entity.setCountry(country);
			}
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

			RegionDAO dao = new RegionDAO(session);
			Region entity;
			String id = formData.getValueSilently("docid");
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new Region();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			RegionTypeDAO rtDao = new RegionTypeDAO(session);
			entity.setType(rtDao.findById(formData.getValue("regiontype")));
			CountryDAO countryDao = new CountryDAO(session);
			Country country = countryDao.findById(UUID.fromString(formData.getValue("country")));
			entity.setCountry(country);
			entity.setLocalizedName(getLocalizedNames(session, formData));

			save(session, entity, dao, isNew);

			finishSaveFormTransact(entity);
		} catch (_Exception | DatabaseException | SecureException e) {
			error(e);
		}
	}

	private void save(_Session ses, Region entity, RegionDAO dao, boolean isNew) throws DatabaseException, SecureException {
		Region foundEntity = dao.findByName(entity.getName());
		if (foundEntity != null && !foundEntity.equals(entity)) {
			_Validation ve = new _Validation();
			ve.addError("name", "unique_error", getLocalizedWord("name_is_not_unique", ses.getLang()));
			setBadRequest();
			setValidation(ve);
			return;
		}

		if (isNew) {
			dao.add(entity);
		} else {
			dao.update(entity);
		}
	}

	@Override
	protected _Validation validate(_WebFormData formData, LanguageCode lang) {
		_Validation v = new _Validation();
		if (formData.getValueSilently("name").isEmpty()) {
			v.addError("name", "required", getLocalizedWord("field_is_empty", lang));
		}
		if (formData.getValueSilently("regiontype").isEmpty() || formData.getValueSilently("regiontype").equals("UNKNOWN")) {
			v.addError("regiontype", "required", getLocalizedWord("field_is_empty", lang));
		}
		if (formData.getValueSilently("country").isEmpty()) {
			v.addError("country", "required", getLocalizedWord("field_is_empty", lang));
		}

		return v;
	}
}
