package reference.page.form;

import java.util.UUID;

import org.eclipse.persistence.exceptions.DatabaseException;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.DepartmentTypeDAO;
import reference.model.DepartmentType;

/**
 * @author Kayra created 03-01-2016
 */

public class DepartmentTypeForm extends ReferenceForm {
	
	@Override
	public void doGET(_Session session, _WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			DepartmentType entity;
			if (!id.isEmpty()) {
				DepartmentTypeDAO dao = new DepartmentTypeDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (DepartmentType) getDefaultEntity(user, new DepartmentType());
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
	public void doPOST(_Session session, _WebFormData formData) {
		try {
			_Validation ve = simpleCheck("name");
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
			
		} catch (_Exception | DatabaseException | SecureException | DAOException e) {
			logError(e);
		}
	}
}
