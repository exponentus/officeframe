package staff.page.form;

import java.util.Date;
import java.util.UUID;

import org.eclipse.persistence.exceptions.DatabaseException;

import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import reference.dao.DepartmentTypeDAO;
import reference.model.DepartmentType;
import staff.dao.DepartmentDAO;
import staff.dao.OrganizationDAO;
import staff.model.Department;
import staff.model.Organization;

/**
 * @author Kayra created 07-01-2016
 */

public class DepartmentForm extends StaffForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		Department entity;
		if (!id.isEmpty()) {
			DepartmentDAO dao = new DepartmentDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = new Department();
			entity.setRegDate(new Date());
			entity.setAuthor(user);
			entity.setName("");
			Organization o = new Organization();
			o.setName("");
			entity.setOrganization(o);
			DepartmentType dt = new DepartmentType();
			dt.setName("");
			entity.setType(dt);
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
			DepartmentDAO dao = new DepartmentDAO(session);
			Department entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new Department();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			DepartmentTypeDAO dtDao = new DepartmentTypeDAO(session);
			DepartmentType dt = dtDao.findById(formData.getValueSilently("departmenttype"));
			entity.setType(dt);
			OrganizationDAO orgDAO = new OrganizationDAO(session);
			entity.setOrganization(orgDAO.findById(formData.getValue("organization")));

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

		if (formData.getValueSilently("organization").isEmpty()) {
			ve.addError("organization", "required", getLocalizedWord("field_is_empty", lang));
		}

		if (formData.getValueSilently("departmenttype").isEmpty()) {
			ve.addError("departmenttype", "required", getLocalizedWord("field_is_empty", lang));
		}

		return ve;
	}
}
