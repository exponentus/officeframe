package reference.page.form;

import administrator.dao.LanguageDAO;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting.*;
import com.exponentus.user.IUser;
import org.eclipse.persistence.exceptions.DatabaseException;
import reference.dao.ClaimantDecisionTypeDAO;
import reference.model.ClaimantDecisionType;

import java.util.UUID;

public class ClaimantDecisionTypeForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		ClaimantDecisionType entity;
		if (!id.isEmpty()) {
			ClaimantDecisionTypeDAO dao = new ClaimantDecisionTypeDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = (ClaimantDecisionType) getDefaultEntity(user, new ClaimantDecisionType());
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
			ClaimantDecisionTypeDAO dao = new ClaimantDecisionTypeDAO(session);
			ClaimantDecisionType entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new ClaimantDecisionType();
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
