package reference.page.form;

import kz.lof.administrator.dao.LanguageDAO;
import kz.lof.exception.SecureException;
import kz.lof.scripting._POJOListWrapper;
import kz.lof.scripting._Session;
import kz.lof.scripting._Validation;
import kz.lof.scripting._WebFormData;
import kz.lof.user.IUser;
import kz.nextbase.script._EnumWrapper;
import kz.nextbase.script._Exception;
import org.eclipse.persistence.exceptions.DatabaseException;
import reference.dao.DepartmentTypeDAO;
import reference.model.DepartmentType;

import java.util.UUID;

/**
 * @author Kayra created 03-01-2016
 */

public class DepartmentTypeForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser user = session.getUser();
		DepartmentType entity;
		if (!id.isEmpty()) {
			DepartmentTypeDAO dao = new DepartmentTypeDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = (DepartmentType) getDefaultEntity(user, new DepartmentType());
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

			String id = formData.getValueSilently("docid");
			DepartmentTypeDAO dao = new DepartmentTypeDAO(session);
			DepartmentType entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new DepartmentType();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));

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
