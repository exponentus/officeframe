package staff.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import staff.dao.OrganizationLabelDAO;
import staff.model.OrganizationLabel;

/**
 * @author Kayra created 10-01-2016
 */

public class OrganizationLabelForm extends StaffForm {
	
	@Override
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		OrganizationLabel entity;
		try {
			if (!id.isEmpty()) {
				OrganizationLabelDAO dao = new OrganizationLabelDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = new OrganizationLabel();
				entity.setAuthor(user);
				entity.setName("");
			}
			addContent(entity);
			addContent(getSimpleActionBar(session, session.getLang()));
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
			OrganizationLabelDAO dao = new OrganizationLabelDAO(session);
			OrganizationLabel entity;
			boolean isNew = id.isEmpty();
			
			if (isNew) {
				entity = new OrganizationLabel();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}
			
			entity.setName(formData.getValue("name"));
			entity.setDescription(formData.getValue("description"));
			entity.setLocName(getLocalizedNames(session, formData));
			
			if (isNew) {
				dao.add(entity);
			} else {
				dao.update(entity);
			}
			
		} catch (WebFormException | SecureException | DAOException e) {
			logError(e);
			setBadRequest();
		}
	}
}
