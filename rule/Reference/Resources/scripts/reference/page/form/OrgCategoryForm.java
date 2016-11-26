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
import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;

public class OrgCategoryForm extends ReferenceForm {
	
	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		OrgCategory entity;
		if (!id.isEmpty()) {
			OrgCategoryDAO dao = new OrgCategoryDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = (OrgCategory) getDefaultEntity(user, new OrgCategory());
		}
		addContent(entity);
		try {
			addContent(new LanguageDAO(session).findAll());
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}
		addContent(getSimpleActionBar(session));
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
			OrgCategoryDAO dao = new OrgCategoryDAO(session);
			OrgCategory entity;
			boolean isNew = id.isEmpty();
			
			if (isNew) {
				entity = new OrgCategory();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}
			
			entity.setName(formData.getValue("name"));
			entity.setLocalizedName(getLocalizedNames(session, formData));
			
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
