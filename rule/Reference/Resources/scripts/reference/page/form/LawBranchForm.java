package reference.page.form;

import java.util.UUID;

import org.eclipse.persistence.exceptions.DatabaseException;

import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.LawBranchDAO;
import reference.model.LawBranch;

public class LawBranchForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		LawBranch entity;
		if (!id.isEmpty()) {
			LawBranchDAO dao = new LawBranchDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = (LawBranch) getDefaultEntity(user, new LawBranch());
		}
		addContent(entity);
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
			LawBranchDAO dao = new LawBranchDAO(session);
			LawBranch entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new LawBranch();
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

		} catch (_Exception | DatabaseException | SecureException e) {
			logError(e);
		}
	}
}
