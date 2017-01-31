package reference.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._EnumWrapper;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.RegionTypeDAO;
import reference.model.RegionType;
import reference.model.constants.RegionCode;

/**
 * @author Kayra created 03-01-2016
 */

public class RegionTypeForm extends ReferenceForm {
	
	@Override
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		try {
			RegionType entity;
			if (!id.isEmpty()) {
				RegionTypeDAO dao = new RegionTypeDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (RegionType) getDefaultEntity(user, new RegionType());
			}
			addContent(entity);
			addContent(new _EnumWrapper(RegionCode.class.getEnumConstants()));
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
			_Validation ve = simpleCheck("name");
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}
			
			String id = formData.getValueSilently("docid");
			RegionTypeDAO dao = new RegionTypeDAO(session);
			RegionType entity;
			boolean isNew = id.isEmpty();
			
			if (isNew) {
				entity = new RegionType();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}
			
			entity.setName(formData.getValue("name"));
			entity.setCode(RegionCode.valueOf(formData.getValueSilently("code", formData.getValue("code"))));
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
}
