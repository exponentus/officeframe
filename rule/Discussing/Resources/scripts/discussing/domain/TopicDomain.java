package discussing.domain;

import administrator.model.User;
import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.common.dto.ACL;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.stream.EntityFileBinder;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import discussing.dao.TopicDAO;
import discussing.model.Comment;
import discussing.model.Topic;
import discussing.model.constants.TopicStatusType;
import staff.model.Employee;
import staff.model.embedded.Observer;

import java.util.List;
import java.util.UUID;

public class TopicDomain extends CommonDomain<Topic> {

    public TopicDomain(_Session ses) throws DAOException {
        super(ses);
        dao = new TopicDAO(ses);
    }

    public Topic composeNew(User author) {
        Topic topic = new Topic();

        topic.setAuthor(author);
        topic.setStatus(TopicStatusType.DRAFT);
        topic.setTitle("");
        topic.setBody("");

        return topic;
    }

    @Override
    public Topic fillFromDto(Topic dto, IValidation<Topic> validation, String formSesId) throws DTOException, DAOException {
        validation.check(dto);

        Topic topic;
        if (dto.isNew()) {
            topic = composeNew(ses.getUser());
        } else {
            topic = dao.findById(dto.getId());
        }

        if (dto.getStatus() == TopicStatusType.UNKNOWN) {
            topic.setStatus(TopicStatusType.DRAFT);
        } else {
            topic.setStatus(dto.getStatus());
        }
        topic.setTitle(dto.getTitle());
        topic.setBody(dto.getBody());
        topic.setTags(dto.getTags());
        topic.setObservers(dto.getObservers());

        calculateReaders(topic);

        return topic;
    }

    public void addComment(Topic topic, Comment comment) {
        comment.setTopic((topic));
        topic.getComments().add(comment);
    }

    public void calculateReaders(Topic topic) {
        topic.resetReadersEditors();

        topic.addReaderEditor(topic.getAuthor());

        List<Observer> observers = topic.getObservers();
        if (observers != null) {
            for (Observer observer : observers) {
                Employee emp = observer.getEmployee();
                topic.addReader(emp.getUserID());
            }
        }
    }

    @Override
    public Topic save(Topic entity) throws SecureException, DAOException, DTOException {
        if (entity.isNew()) {
            entity = dao.add(entity);
        } else {
            entity = dao.update(entity);
        }

        EntityFileBinder<Topic, UUID> fileBinder = new EntityFileBinder<Topic, UUID>(dao, ses, entity);
        fileBinder.run();

        return entity;
    }

    public boolean topicCanBeDeleted(Topic topic) {
        return !topic.isNew() && topic.isEditable() && topic.getStatus() == TopicStatusType.DRAFT;
    }

    public Outcome getOutcome(Topic topic) {
        Outcome outcome = new Outcome();

        if (topic.isNew()) {
            outcome.setTitle(Environment.vocabulary.getWord("topic", ses.getLang()));
        } else {
            outcome.setTitle(Environment.vocabulary.getWord("topic", ses.getLang()) + " " + topic.getTitle());
        }

        outcome.addPayload("contentTitle", "topic");
        outcome.addPayload(topic);
        if (!topic.isNew()) {
            outcome.addPayload(new ACL(topic));
        }

        return outcome;
    }
}
