package discussing.services;

import com.exponentus.common.domain.IValidation;
import com.exponentus.common.service.EntityService;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._ActionBar;
import discussing.dao.CommentDAO;
import discussing.dao.TopicDAO;
import discussing.domain.TopicDomain;
import discussing.model.Comment;
import discussing.model.Topic;
import discussing.ui.ActionFactory;
import staff.dao.EmployeeDAO;
import staff.model.Employee;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
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
            TopicDAO topicDAO = new TopicDAO(session);

            ViewPage<Topic> vp = topicDAO.findViewPage(sortParams, params.getPage(), session.getPageSize());

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
                topic = dao.findByIdentefier(id);
                if (topic == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

            Map<Long, Employee> emps = empDao.findAll(false).getResult().stream()
                    .collect(Collectors.toMap(Employee::getUserID, Function.identity(), (e1, e2) -> e1));

            Outcome outcome = topicDomain.getOutcome(topic);
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload("employees", emps);

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
            Topic topic = topicDAO.findByIdentefier(id);

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
            Topic topic = topicDAO.findByIdentefier(id);

            comment.setId(null);
            comment.setTopic(topic);

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
            Topic topic = topicDAO.findByIdentefier(id);

            comment.setTopic(topic);

            commentDAO.save(comment);

            Outcome outcome = new Outcome();
            outcome.addPayload(comment);

            return Response.ok(outcome).build();
        } catch (Exception e) {
            return responseException(e);
        }
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
