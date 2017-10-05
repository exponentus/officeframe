package monitoring.services;

import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import monitoring.init.AppConst;

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

        Outline co = new Outline("", "common");
        co.addEntry(new OutlineEntry("user_activities", "", "user_activities", AppConst.BASE_URL + "user-activities"));
        co.addEntry(new OutlineEntry("last_logins", "", "last_logins", AppConst.BASE_URL + "user-activities/last-visits"));
        co.addEntry(new OutlineEntry("count_of_records", "", "count_of_records", AppConst.BASE_URL + "user-activities/count-of-records"));

        LinkedList<IOutcomeObject> list = new LinkedList<>();
        list.add(co);

        Outcome outcome = new Outcome();
        outcome.addPayload("nav", list);

        return Response.ok(outcome).build();
    }
}
