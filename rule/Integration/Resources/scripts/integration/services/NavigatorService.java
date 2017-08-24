package integration.services;

import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import integration.init.AppConst;

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
        LinkedList<IOutcomeObject> list = new LinkedList<>();

        Outline co = new Outline("", "common");

        co.addEntry(new OutlineEntry("services", "", "services", AppConst.BASE_URL + "services"));

        list.add(co);

        Outcome outcome = new Outcome();
        outcome.addPayload("nav", list);

        return Response.ok(outcome).build();
    }
}
