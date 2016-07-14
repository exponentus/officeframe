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
import reference.dao.BuildingMaterialDAO;
import reference.model.BuildingMaterial;

public class BuildingMaterialForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		BuildingMaterial entity;
		if (!id.isEmpty()) {
			BuildingMaterialDAO dao = new BuildingMaterialDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = (BuildingMaterial) getDefaultEntity(user, new BuildingMaterial());
		}
		addContent(entity);
		addContent(new LanguageDAO(session).findAll());
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
			BuildingMaterialDAO dao = new BuildingMaterialDAO(session);
			BuildingMaterial entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new BuildingMaterial();
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
			logError(e);
		}
	}
}
