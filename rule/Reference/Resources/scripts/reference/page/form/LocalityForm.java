package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.DistrictDAO;
import reference.dao.LocalityDAO;
import reference.dao.LocalityTypeDAO;
import reference.dao.RegionDAO;
import reference.model.Locality;
import reference.model.LocalityType;
import reference.model.Region;
import reference.model.constants.LocalityCode;

/**
 * @author Kayra created 03-01-2016
 */

public class LocalityForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		try {
			Locality entity;
			if (!id.isEmpty()) {
				LocalityDAO dao = new LocalityDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = new Locality();
				entity.setAuthor(user);
				entity.setName("");
				Region region = new Region();
				region.setName("");
				entity.setRegion(region);
				LocalityType regionType = new LocalityTypeDAO(session).findByCode(LocalityCode.CITY);
				entity.setType(regionType);
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

			String id = formData.getValueSilently("docid");
			LocalityDAO dao = new LocalityDAO(session);

			Locality entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new Locality();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			LocalityTypeDAO localityTypeDAO = new LocalityTypeDAO(session);
			entity.setType(localityTypeDAO.findById(formData.getValue("localitytype")));
			RegionDAO regionDAO = new RegionDAO(session);
			entity.setRegion(regionDAO.findById(formData.getValueSilently("region")));

			String districtId = formData.getValueSilently("district");
			if (!districtId.isEmpty()) {
				DistrictDAO districtDAO = new DistrictDAO(session);
				entity.setDistrict(districtDAO.findById(districtId));
			} else {

			}
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

	protected _Validation validate(WebFormData formData, LanguageCode lang) {
		_Validation ve = simpleCheck("name");

		if (formData.getValueSilently("region").isEmpty()) {
			ve.addError("region", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (formData.getValueSilently("localitytype").isEmpty()) {
			ve.addError("localitytype", "required", getLocalizedWord("field_is_empty", lang));
		}

		return ve;
	}
}
