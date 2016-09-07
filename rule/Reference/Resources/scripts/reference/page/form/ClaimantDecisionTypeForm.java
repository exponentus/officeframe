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
import reference.dao.ClaimantDecisionTypeDAO;
import reference.model.ClaimantDecisionType;

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
		addContent(new LanguageDAO(session).findAll());
		addContent(getSimpleActionBar(session));
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

		} catch (_Exception | DatabaseException | SecureException e) {
			logError(e);
		}
	}
}
