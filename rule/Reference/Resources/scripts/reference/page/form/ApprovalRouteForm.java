package reference.page.form;

import java.util.HashMap;
import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.WebFormException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.user.IUser;

import administrator.dao.LanguageDAO;
import reference.dao.ApprovalRouteDAO;
import reference.model.ApprovalRoute;
import reference.model.constants.ApprovalSchemaType;

public class ApprovalRouteForm extends ReferenceForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			ApprovalRoute entity;
			if (!id.isEmpty()) {
				ApprovalRouteDAO dao = new ApprovalRouteDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = new ApprovalRoute();
				entity.setAuthor(user);
				entity.setName("");
				entity.setSchema(ApprovalSchemaType.REJECT_IF_NO);
				entity.setLocalizedDescr(new HashMap<LanguageCode, String>());
				entity.setLocName(new HashMap<LanguageCode, String>());
				entity.setCategory("");
				entity.setCode("");
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
	public void doPOST(_Session session, WebFormData formData) {
		try {
			_Validation ve = simpleCheck("name");
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}

			String id = formData.getValueSilently("docid");
			ApprovalRouteDAO dao = new ApprovalRouteDAO(session);
			ApprovalRoute entity;
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new ApprovalRoute();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}

			entity.setName(formData.getValue("name"));
			entity.setLocName(getLocalizedNames(session, formData));

			try {
				if (isNew) {
					dao.add(entity);
				} else {
					dao.update(entity);
				}

			} catch (DAOException e) {
				if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
					ve = new _Validation();
					ve.addError("code", "unique_error", getLocalizedWord("code_is_not_unique", session.getLang()));
					setBadRequest();
					setValidation(ve);
					return;
				} else {
					throw e;
				}
			}
		} catch (WebFormException | SecureException | DAOException e) {
			logError(e);
			setBadRequest();
		}
	}

}
