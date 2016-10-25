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
import reference.dao.TagDAO;
import reference.model.Tag;

/**
 * @author Kayra created 28-01-2016
 */

public class TagForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		TagDAO dao = new TagDAO(session);
		Tag entity;
		if (!id.isEmpty()) {
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = (Tag) getDefaultEntity(user, new Tag());
		}
		addContent(entity);
		addContent("category", dao.findAllCategories());
		addContent(new LanguageDAO(session).findAll());
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
			TagDAO dao = new TagDAO(session);
			Tag entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new Tag();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setColor(formData.getValue("color"));
			entity.setCategory(formData.getValue("category"));
			entity.setHidden(formData.getBoolSilently("hidden"));
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
