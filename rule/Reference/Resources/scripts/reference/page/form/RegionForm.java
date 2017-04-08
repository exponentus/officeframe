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
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		try {
			RegionTypeDAO regionTypeDAO = new RegionTypeDAO(session);
			Region entity;
			if (!id.isEmpty()) {
				RegionDAO dao = new RegionDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				CountryDAO cDao = new CountryDAO(session);
				entity = new Region();
				entity.setAuthor(user);
				entity.setName("");
				RegionType regionType = regionTypeDAO.findByCode(RegionCode.REGION);
				entity.setType(regionType);
				Country country = cDao.findByCode(CountryCode.KZ);
				if (country != null) {
					entity.setCountry(country);
				}
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
			entity.setLocName(getLocalizedNames(session, formData));

			save(session, entity, dao, isNew);

		} catch (WebFormException | SecureException | DAOException e) {
			logError(e);
		}
	}

	private void save(_Session ses, Region entity, RegionDAO dao, boolean isNew) throws SecureException, DAOException {

		try {
			if (isNew) {
				dao.add(entity);
			} else {
				dao.update(entity);
			}
		} catch (DAOException e) {
			if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
				_Validation ve = new _Validation();
				ve.addError("code", "unique_error", getLocalizedWord("name_is_not_unique", ses.getLang()));
				setBadRequest();
				setValidation(ve);
				return;
			} else {
				throw e;
			}
		}
	}

	protected _Validation validate(WebFormData formData, LanguageCode lang) {
		_Validation v = simpleCheck("name");

		if (formData.getValueSilently("regiontype").isEmpty()
				|| formData.getValueSilently("regiontype").equals("UNKNOWN")) {
			v.addError("regiontype", "required", getLocalizedWord("field_is_empty", lang));
		}
		if (formData.getValueSilently("country").isEmpty()) {
			v.addError("country", "required", getLocalizedWord("field_is_empty", lang));
		}

		return v;
	}
}
