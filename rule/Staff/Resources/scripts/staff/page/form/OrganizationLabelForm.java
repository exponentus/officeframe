package staff.page.form;

import java.util.UUID;

import org.eclipse.persistence.exceptions.DatabaseException;

import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import staff.dao.OrganizationLabelDAO;
import staff.model.OrganizationLabel;

/**
 * @author Kayra created 10-01-2016
 */

public class OrganizationLabelForm extends StaffForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		OrganizationLabel entity;
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
			entity.setLocalizedName(getLocalizedNames(session, formData));

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
}
