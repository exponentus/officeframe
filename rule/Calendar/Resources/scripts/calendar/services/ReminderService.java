package calendar.services;

import calendar.domain.ReminderDomain;
import calendar.model.Reminder;
import com.exponentus.common.service.EntityService;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("reminders")
@Produces(MediaType.APPLICATION_JSON)
public class ReminderService extends EntityService<Reminder, ReminderDomain> {



}
