package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.DistrictDAO;
import reference.dao.RegionDAO;
import reference.model.District;
import reference.model.Region;

/**
 * @author Kayra created 03-01-2016
 */

public class DistrictForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser user = session.getUser();
			District entity;
			if (!id.isEmpty()) {
				DistrictDAO dao = new DistrictDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (District) getDefaultEntity(user, new District());
				RegionDAO cDao = new RegionDAO(session);
				Region region = null;
				try {
					region = cDao.findByName("Алматы");
				} catch (DAOException e) {
					logError(e);
				}
				entity.setRegion(region);
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
			_Validation ve = simpleCheck("name");
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			String id = formData.getValueSilently("docid");
			DistrictDAO dao = new DistrictDAO(session);
			RegionDAO regionDAO = new RegionDAO(session);
			District entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new District();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValueSilently("name"));
			entity.setRegion(regionDAO.findById(UUID.fromString(formData.getValueSilently("region"))));
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
}
