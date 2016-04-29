package reference.page.form;

import administrator.dao.LanguageDAO;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.*;
import com.exponentus.user.IUser;
import org.eclipse.persistence.exceptions.DatabaseException;
import reference.dao.LawBranchDAO;
import reference.model.LawBranch;

import java.util.UUID;

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

			finishSaveFormTransact(entity);
		} catch (_Exception | DatabaseException | SecureException e) {
			error(e);
		}
	}
}
