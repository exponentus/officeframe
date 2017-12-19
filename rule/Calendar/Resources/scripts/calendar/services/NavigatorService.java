package calendar.services;

import calendar.init.ModuleConst;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;

@Path("navigator")
public class NavigatorService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNav() {

        Outline co = new Outline("calendar", "calendar");
        co.addEntry(new OutlineEntry("events", "", "events", ModuleConst.BASE_URL + "events"));

        Outline r = new Outline("reminder", "reminder");
        r.addEntry(new OutlineEntry("reminder_templates", "", "reminder_templates", ModuleConst.BASE_URL + "reminders"));

        LinkedList<IOutcomeObject> list = new LinkedList<>();
        list.add(co);
        list.add(r);

        Outcome outcome = new Outcome();
        outcome.addPayload("nav", list);

        return Response.ok(outcome).build();
    }
}
