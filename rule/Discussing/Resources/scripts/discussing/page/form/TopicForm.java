package discussing.page.form;

import java.util.UUID;

import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scheduler._EnumWrapper;
import com.exponentus.scripting._Exception;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.scripting.event._DoForm;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import com.exponentus.user.IUser;

import discussing.dao.TopicDAO;
import discussing.model.Topic;
import discussing.model.constants.TopicStatusType;

public class TopicForm extends _DoForm {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		String id = formData.getValueSilently("docid");
		IUser<Long> user = session.getUser();
		Topic entity;
		if (!id.isEmpty()) {
			TopicDAO dao = new TopicDAO(session);
			entity = dao.findById(UUID.fromString(id));
		} else {
			entity = new Topic();
			entity.setAuthor(user);
			entity.setSubject("");
			entity.setStatus(TopicStatusType.DRAFT);
		}
		addContent(entity);
		addContent(new _EnumWrapper<>(TopicStatusType.class.getEnumConstants()));
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

			TopicDAO dao = new TopicDAO(session);
			Topic entity;
			String id = formData.getValueSilently("docid");
			boolean isNew = id.isEmpty();

			if (isNew) {
				entity = new Topic();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}
			TopicStatusType status = TopicStatusType.valueOf(formData.getValueSilently("status"));
			entity.setStatus(status);
			entity.setSubject(formData.getValue("subject"));

			save(session, entity, dao, isNew);

		} catch (_Exception | SecureException e) {
			logError(e);
		}
	}

	private void save(_Session ses, Topic entity, TopicDAO dao, boolean isNew) throws SecureException {

		if (isNew) {
			dao.add(entity);
		} else {
			dao.update(entity);
		}
	}

	private _Validation validate(_WebFormData formData, LanguageCode lang) {
		_Validation ve = new _Validation();
		if (formData.getValueSilently("subject").isEmpty()) {
			ve.addError("subject", "required", getLocalizedWord("field_is_empty", lang));
		}

		return ve;
	}

	private IOutcomeObject getSimpleActionBar(_Session ses) {
		_ActionBar actionBar = new _ActionBar(ses);
		LanguageCode lang = ses.getLang();

		actionBar.addAction(new _Action(getLocalizedWord("save_close", lang), "", _ActionType.SAVE_AND_CLOSE));

		actionBar.addAction(new _Action(getLocalizedWord("close", lang), "", _ActionType.CLOSE));
		return actionBar;

	}
}
