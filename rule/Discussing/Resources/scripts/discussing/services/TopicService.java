package discussing.services;

import administrator.model.User;
import com.exponentus.common.domain.IValidation;
import com.exponentus.common.service.EntityService;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.actions._ActionBar;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import discussing.dao.CommentDAO;
import discussing.dao.TopicDAO;
import discussing.dao.filter.TopicFilter;
import discussing.domain.TopicDomain;
import discussing.model.Comment;
import discussing.model.Topic;
import discussing.model.constants.TopicStatusType;
import discussing.ui.ActionFactory;
import discussing.ui.ViewOptions;
import reference.model.Tag;
import staff.dao.EmployeeDAO;
import staff.model.Employee;
import staff.model.embedded.Observer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("topics")
@Produces(MediaType.APPLICATION_JSON)
public class TopicService extends EntityService<Topic, TopicDomain> {

    @GET
    public Response getViewPage() {

        _Session session = getSession();
        try {
            WebFormData params = getWebFormData();
            SortParams sortParams = params.getSortParams(SortParams.asc("regDate"));
            TopicFilter filter = setUpTopicFilter(session, params, new TopicFilter());
            TopicDAO topicDAO = new TopicDAO(session);
            ViewPage<Topic> vp = topicDAO.findViewPage(filter, sortParams, params.getPage(), session.getPageSize());
            ViewOptions viewOptions = new ViewOptions();
            vp.setViewPageOptions(viewOptions.getTopicOptions());
            vp.setFilter(viewOptions.getTopicFilter(session));

            ActionFactory actionFactory = new ActionFactory();
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(actionFactory.newTopic);
            actionBar.addAction(actionFactory.refreshVew);

            Outcome outcome = new Outcome();
            outcome.setId("topics");
            outcome.setTitle("topics");
            outcome.addPayload(actionBar);
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (Exception e) {
            return responseException(e);
        }
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") String id) {
        _Session session = getSession();
        try {
            EmployeeDAO empDao = new EmployeeDAO(session);
            TopicDAO dao = new TopicDAO(session);
            Topic topic;
            TopicDomain topicDomain = new TopicDomain(session);
            boolean isNew = "new".equals(id);

            if (isNew) {
                topic = topicDomain.composeNew(session.getUser());
            } else {
                topic = dao.findByIdentifier(id);
                if (topic == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

            Map<Long, Employee> emps = empDao.findAll(false).getResult().stream()
                    .collect(Collectors.toMap(Employee::getUserID, Function.identity(), (e1, e2) -> e1));

            ActionFactory actionFactory = new ActionFactory();
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(actionFactory.close);
            actionBar.addAction(actionFactory.saveAndClose);

            Outcome outcome = topicDomain.getOutcome(topic);
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload("employees", emps);
            outcome.addPayload(actionBar);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @Override
    public Response saveForm(Topic dto) {
        try {
            TopicDomain domain = new TopicDomain(getSession());
            Topic entity = domain.fillFromDto(dto, new Validation(), getWebFormData().getFormSesId());
            domain.save(entity);

            Outcome outcome = domain.getOutcome(entity);

            return Response.ok(outcome).build();
        } catch (DTOException e) {
            return responseValidationError(e);
        } catch (DAOException | SecureException e) {
            return responseException(e);
        }
    }

    @GET
    @Path("{id}/comments")
    public Response getTopicComments(@PathParam("id") String id) {
        _Session session = getSession();
        try {
            WebFormData params = getWebFormData();
            SortParams sortParams = params.getSortParams(SortParams.asc("regDate"));
            TopicDAO topicDAO = new TopicDAO(session);
            Topic topic = topicDAO.findByIdentifier(id);

            Outcome outcome = new Outcome();
            outcome.addPayload("comments", topic.getComments());

            return Response.ok(outcome).build();
        } catch (Exception e) {
            return responseException(e);
        }
    }

    @POST
    @Path("{id}/comments")
    public Response addComment(@PathParam("id") String id, Comment comment) {
        _Session session = getSession();
        try {
            CommentDAO commentDAO = new CommentDAO(session);
            TopicDAO topicDAO = new TopicDAO(session);
            Topic topic = topicDAO.findByIdentifier(id);

            comment.setId(null);
            comment.setTopic(topic);
            comment.addReaderEditor(session.getUser());
            comment.addReader(topic.getAuthor());

            List<Observer> observers = topic.getObservers();
            if (observers != null) {
                for (Observer observer : observers) {
                    Employee emp = observer.getEmployee();
                    comment.addReader(emp.getUserID());
                }
            }
            commentDAO.save(comment);

            Outcome outcome = new Outcome();
            outcome.addPayload(comment);

            return Response.ok(outcome).build();
        } catch (Exception e) {
            return responseException(e);
        }
    }

    @PUT
    @Path("{id}/comments")
    public Response updateComment(@PathParam("id") String id, Comment comment) {
        _Session session = getSession();
        try {
            CommentDAO commentDAO = new CommentDAO(session);
            TopicDAO topicDAO = new TopicDAO(session);
            Topic topic = topicDAO.findByIdentifier(id);

            comment.setTopic(topic);
            comment.addReader(topic.getAuthor());
            List<Observer> observers = topic.getObservers();
            if (observers != null) {
                for (Observer observer : observers) {
                    Employee emp = observer.getEmployee();
                    comment.addReader(emp.getUserID());
                }
            }
            commentDAO.save(comment);

            Outcome outcome = new Outcome();
            outcome.addPayload(comment);

            return Response.ok(outcome).build();
        } catch (Exception e) {
            return responseException(e);
        }
    }

    public TopicFilter setUpTopicFilter(_Session session, WebFormData formData, TopicFilter filter) {

        String statusName = formData.getValueSilently("status");
        if (!statusName.isEmpty()) {
            filter.setStatus(TopicStatusType.valueOf(statusName));
        }

        long authorId = formData.getNumberValueSilently("author", -1);
        if (authorId > 0) {
            User authorUser = new User();
            authorUser.setId(authorId);
            filter.setAuthor(authorUser);
        }

        String slug = formData.getValueSilently("slug");
        switch (slug) {
            case "my":
                filter.setAuthor((User) session.getUser());
                break;
            default:
                break;
        }

        if (formData.containsField("tags")) {
            List<Tag> tags = new ArrayList<>();
            String[] tagIds = formData.getListOfValuesSilently("tags");
            for (String tid : tagIds) {
                Tag tag = new Tag();
                tag.setId(UUID.fromString(tid));
                tags.add(tag);
            }
            filter.setTags(tags);
        }

        return filter;
    }

    private class Validation implements IValidation<Topic> {

        @Override
        public void check(Topic dto) throws DTOException {
            DTOException ve = new DTOException();

            if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
                ve.addError("title", "required", "field_is_empty");
            }
            if (dto.getBody() == null) {
                ve.addError("body", "required", "field_is_empty");
            }

            if (ve.hasError()) {
                throw ve;
            }
        }
    }
}
