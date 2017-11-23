package calendar.services;

import calendar.domain.EventDomain;
import calendar.model.Event;
import com.exponentus.common.service.EntityService;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("events")
@Produces(MediaType.APPLICATION_JSON)
public class EventService extends EntityService<Event, EventDomain> {



}
