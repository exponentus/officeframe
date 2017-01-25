package reference.page.form;

import java.util.UUID;

import org.eclipse.persistence.exceptions.DatabaseException;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.WebFormData;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.TextTemplateDAO;
import reference.model.TextTemplate;

public class TextTemplateForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		try {
			TextTemplate entity;
			if (!id.isEmpty()) {
				TextTemplateDAO dao = new TextTemplateDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = (TextTemplate) getDefaultEntity(user, new TextTemplate());
				entity.setCategory("");
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
			_Validation ve = simpleCheck("name");
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			String id = formData.getValueSilently("docid");
			TextTemplateDAO dao = new TextTemplateDAO(session);
			TextTemplate entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new TextTemplate();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setCategory(formData.getValue("category"));
			entity.setLocalizedName(getLocalizedNames(session, formData));

			if (isNew) {
				dao.add(entity);
			} else {
				dao.update(entity);
			}

		} catch (WebFormException | DatabaseException | SecureException | DAOException e) {
			logError(e);
		}
	}
}
