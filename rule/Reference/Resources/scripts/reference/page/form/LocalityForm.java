package reference.page.form;

import administrator.dao.LanguageDAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;
import reference.dao.LocalityDAO;
import reference.dao.LocalityTypeDAO;
import reference.dao.RegionDAO;
import reference.model.Locality;
import reference.model.LocalityType;
import reference.model.Region;
import reference.model.constants.LocalityCode;

import java.util.UUID;

/**
 * @author Kayra created 03-01-2016
 */

public class LocalityForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser user = session.getUser();
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

			entity.setName(formData.getValueSilently("name"));
			LocalityTypeDAO localityTypeDAO = new LocalityTypeDAO(session);
			entity.setType(localityTypeDAO.findByIdentefier(formData.getValueSilently("localitytype")));
			RegionDAO regionDAO = new RegionDAO(session);
			entity.setRegion(regionDAO.findByIdentefier(formData.getValueSilently("region")));

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
