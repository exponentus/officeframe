package reference.page.form;

import java.util.UUID;

import org.eclipse.persistence.exceptions.DatabaseException;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.WebFormData;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.CityDistrictDAO;
import reference.dao.LocalityDAO;
import reference.model.CityDistrict;
import reference.model.Locality;

public class CityDistrictForm extends ReferenceForm {
	
	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			CityDistrict entity;
			if (!id.isEmpty()) {
				CityDistrictDAO dao = new CityDistrictDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (CityDistrict) getDefaultEntity(user, new CityDistrict());
				Locality locality = new Locality();
				locality.setName("");
				entity.setLocality(locality);
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
			CityDistrictDAO dao = new CityDistrictDAO(session);
			CityDistrict entity;
			boolean isNew = id.isEmpty();
			
			if (isNew) {
				entity = new CityDistrict();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}
			
			entity.setName(formData.getValue("name"));
			entity.setLocName(getLocalizedNames(session, formData));
			LocalityDAO localityDAO = new LocalityDAO(session);
			entity.setLocality(localityDAO.findById(formData.getValue("locality")));
			
			CityDistrict foundEntity = dao.findByName(entity.getName());
			if (foundEntity != null && !foundEntity.equals(entity)
					&& foundEntity.getLocality().equals(entity.getLocality())) {
				ve = new _Validation();
				ve.addError("name", "unique", getLocalizedWord("name_is_not_unique", session.getLang()));
				setBadRequest();
				setValidation(ve);
				return;
			}
			
			if (isNew) {
				dao.add(entity);
			} else {
				dao.update(entity);
			}
			
		} catch (WebFormException | DatabaseException | SecureException | DAOException e) {
			logError(e);
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
