package discussing.page.form;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.event._DoForm;
import com.exponentus.user.IUser;

import discussing.dao.CommentDAO;
import discussing.dao.TopicDAO;
import discussing.model.Comment;
import discussing.model.Topic;

public class CommentForm extends _DoForm {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		try {
			String id = formData.getValueSilently("docid");
			IUser<Long> user = session.getUser();
			Comment entity;
			String topicId = formData.getValueSilently("topicid");
			TopicDAO topicDAO = new TopicDAO(session);
			Topic topic = topicDAO.findByIdentefier(topicId);
			if (!id.isEmpty()) {
				CommentDAO dao = new CommentDAO(session);
				entity = dao.findById(UUID.fromString(id));
			} else {
				entity = new Comment();
				entity.setAuthor(user);
			}
			entity.setTask(topic);
			addContent(entity);
			addContent(getSimpleActionBar(session));
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
		}
	}

	@Override
	public void doPOST(_Session session, WebFormData formData) {
		devPrint(formData);
		try {
			_Validation ve = validate(formData, session.getLang());
			if (ve.hasError()) {
				setBadRequest();
				setValidation(ve);
				return;
			}
			CommentDAO dao = new CommentDAO(session);
			Comment entity;
			String id = formData.getValueSilently("docid");
			boolean isNew = id.isEmpty();
			String comment = formData.getValueSilently("comment");
			String topicId = formData.getValueSilently("topicid");
			TopicDAO topicDAO = new TopicDAO(session);
			Topic topic = topicDAO.findByIdentefier(topicId);
			if (isNew) {
				entity = new Comment();
			} else {
				entity = dao.findById(UUID.fromString(id));
			}
			entity.setComment(comment);
			entity.setTask(topic);
			save(session, entity, dao, isNew);
		} catch (SecureException | DAOException e) {
			logError(e);
		}
	}

	private void save(_Session ses, Comment entity, CommentDAO dao, boolean isNew)
			throws SecureException, DAOException {
		if (isNew) {
			dao.add(entity);
		} else {
			dao.update(entity);
		}
	}

	private _Validation validate(WebFormData formData, LanguageCode lang) {
		_Validation ve = new _Validation();
		if (formData.getValueSilently("comment").isEmpty()) {
			ve.addError("comment", "required", getLocalizedWord("field_is_empty", lang));
		}

		return ve;
	}

}
