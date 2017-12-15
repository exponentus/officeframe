package calendar.services;

import calendar.dao.EventDAO;
import calendar.dao.filter.EventFilter;
import calendar.domain.EventDomain;
import calendar.model.Event;
import com.exponentus.common.domain.IValidation;
import com.exponentus.common.model.constants.PriorityType;
import com.exponentus.common.service.EntityService;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
import com.exponentus.common.ui.view.ViewPageOptions;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("events")
@Produces(MediaType.APPLICATION_JSON)
public class EventService extends EntityService<Event, EventDomain> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = params.getNumberValueSilently("limit", session.getPageSize());

        try {
            EventFilter eventFilter = new EventFilter();
            setupFilter(eventFilter, params);

            SortParams sortParams = SortParams.valueOf(params.getStringValueSilently("regDate", "-regDate"));
            EventDAO dao = new EventDAO(session);
            ViewPage<Event> vp = dao.findViewPage(eventFilter, sortParams, params.getPage(), pageSize);

            ViewPageOptions vo = new ViewPageOptions();
            ViewColumnGroup cg = new ViewColumnGroup();
            cg.setClassName("vw-35");
            cg.add(new ViewColumn("title"));
            ViewColumnGroup cg2 = new ViewColumnGroup();
            cg2.setClassName("vw-35");
            cg2.add(new ViewColumn("eventTime").name("event_time").type(ViewColumnType.date).format("DD.MM.YYYY").sortBoth());
            cg2.add(new ViewColumn("priority").type(ViewColumnType.translate));
            ViewColumnGroup cg3 = new ViewColumnGroup();
            cg3.setClassName("vw-30");
            cg3.add(new ViewColumn("tags").type(ViewColumnType.localizedName));
            List<ViewColumnGroup> list = new ArrayList<>();
            list.add(cg);
            list.add(cg2);
            list.add(cg3);
            vo.setRoot(list);
            vp.setViewPageOptions(vo);

            Outcome outcome = new Outcome();
            outcome.setTitle("events");
            outcome.addPayload("contentTitle", "events");
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
            EventDomain domain = new EventDomain(session);
            Event entity;

            boolean isNew = "new".equals(id);
            if (isNew) {
                entity = domain.composeNew(session.getUser());
            } else {
                entity = dao.findByIdentifier(id);
                if (entity == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

            Outcome outcome = domain.getOutcome(entity);
            outcome.setTitle("event");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload("contentTitle", "event");
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
            EventDomain omd = new EventDomain(getSession());
            Event entity = omd.fillFromDto(dto, new Validation(), getWebFormData().getFormSesId());
            Outcome outcome = omd.getOutcome(omd.save(entity));
            return Response.ok(outcome).build();
        } catch (DTOException e) {
            return responseValidationError(e);
        } catch (DAOException | SecureException e) {
            return responseException(e);
        }
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
//            Calendar weekBegin = Calendar.getInstance();
//            weekBegin.set(Calendar.WEEK_OF_YEAR, yearWeekNum);
//            weekBegin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//
//            Calendar weekEnd = Calendar.getInstance();
//            weekEnd.set(Calendar.WEEK_OF_YEAR, yearWeekNum);
//            weekEnd.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        if (params.containsField("eventStart")) {
            filter.setEventStart(params.getDateSilently("eventStart"));
        }

        if (params.containsField("eventEnd")) {
            filter.setEventEnd(params.getDateSilently("eventEnd"));
        }
    }
}
