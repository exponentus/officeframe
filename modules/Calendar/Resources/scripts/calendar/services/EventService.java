package calendar.services;

import calendar.dao.EventDAO;
import calendar.dao.filter.EventFilter;
import calendar.dto.converter.EventDtoConverter;
import calendar.model.Event;
import calendar.ui.ViewOptions;
import com.exponentus.common.domain.IValidation;
import com.exponentus.common.model.constants.PriorityType;
import com.exponentus.common.service.AbstractService;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.model.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("events")
@Produces(MediaType.APPLICATION_JSON)
public class EventService extends AbstractService<Event> {

    @GET
    public Response getViewPage() {
        EventFilter eventFilter = new EventFilter();
        setupFilter(eventFilter, getWebFormData());

        return getViewPage(eventFilter);
    }

    @POST
    public Response getViewPage(EventFilter eventFilter) {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = params.getNumberValueSilently("limit", session.getPageSize());
        SortParams sortParams = SortParams.valueOf(params.getStringValueSilently("sort", "-eventTime"));

        try {
            EventDAO dao = new EventDAO(session);
            ViewPage<Event> vp = dao.findViewPage(eventFilter, sortParams, params.getPage(), pageSize);
            vp.setResult(new EventDtoConverter().convert(vp.getResult()));

            ViewOptions vo = new ViewOptions();
            vp.setViewPageOptions(vo.getEventOptions());
            vp.setFilter(vo.getEventFilter(session));

            Outcome outcome = new Outcome();
            outcome.setTitle("events");
            outcome.setPayloadTitle("events");
            outcome.addPayload(vp);
            outcome.addPayload(getDefaultViewActionBar());

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") String id) {
        _Session session = getSession();
        try {
            EventDAO dao = new EventDAO(session);
            Event entity;

            boolean isNew = "new".equals(id);
            if (isNew) {
                entity = new Event();
                entity.setAuthor(session.getUser());
                entity.setTitle("");
            } else {
                entity = dao.findById(id);
                if (entity == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

            Outcome outcome = getOutcome(entity);
            outcome.setTitle("event");
            outcome.setFSID(getWebFormData().getFormSesId());
            outcome.setPayloadTitle("event");
            outcome.addPayload(getDefaultFormActionBar(entity));
            outcome.addPayload("priorityTypes", PriorityType.values());

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @Override
    public Response saveForm(Event dto) {
        try {
            EventDAO dao = new EventDAO(getSession());
            Event entity = fillFromDto(dto, new Validation(), getWebFormData().getFormSesId());
            Outcome outcome = getOutcome(dao.save(entity));
            return Response.ok(outcome).build();
        } catch (DTOException e) {
            return responseValidationError(e);
        } catch (DAOException | SecureException e) {
            return responseException(e);
        }
    }

    @Override
    public Event fillFromDto(Event dto, IValidation<Event> validation, String formSesId) throws DTOException, DAOException {
        validation.check(dto);

        Event entity;

        if (dto.isNew()) {
            entity = new Event();
        } else {
            entity = dao.findById(dto.getId());
        }

        entity.setTitle(dto.getTitle());
        entity.setEventTime(dto.getEventTime());
        entity.setObservers(dto.getObservers());
        entity.setDescription(dto.getDescription());
        entity.setPriority(dto.getPriority());
        entity.setReminder(dto.getReminder());
        entity.setRelatedURL(dto.getRelatedURL());
        entity.setTags(dto.getTags());

        if (entity.isNew()) {
            entity.setAuthor(getSession().getUser());
        }
        entity.setAttachments(getActualAttachments(entity.getAttachments(), dto.getAttachments()));
        calculateReadersEditors(entity);
        return entity;
    }

    private class Validation implements IValidation<Event> {

        @Override
        public void check(Event dto) throws DTOException {
            DTOException e = new DTOException();

            if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
                e.addError("title", "required", "field_is_empty");
            }
            if (dto.getEventTime() == null) {
                e.addError("eventTime", "required", "field_is_empty");
            }
            if (dto.getReminder() == null) {
                e.addError("reminder", "required", "field_is_empty");
            }
            if (e.hasError()) {
                throw e;
            }
        }
    }

    private void setupFilter(EventFilter filter, WebFormData params) {
        if (params.containsField("eventStart")) {
            filter.setEventStart(params.getDateSilently("eventStart"));
        }

        if (params.containsField("eventEnd")) {
            filter.setEventEnd(params.getDateSilently("eventEnd"));
        }

        String taskPriority = params.getValueSilently("priority");
        if (!taskPriority.isEmpty()) {
            filter.setPriority(PriorityType.valueOf(taskPriority));
        }

        if (params.containsField("tags")) {
            List<Tag> tags = new ArrayList<>();
            String[] tagIds = params.getListOfValuesSilently("tags");
            for (String tid : tagIds) {
                Tag tag = new Tag();
                tag.setId(UUID.fromString(tid));
                tags.add(tag);
            }
            filter.setTags(tags);
        }
    }
}
