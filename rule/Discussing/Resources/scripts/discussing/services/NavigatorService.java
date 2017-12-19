package discussing.services;

import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import discussing.init.ModuleConst;

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
        co.addEntry(new OutlineEntry("topics_all", "", "topics_all", ModuleConst.BASE_URL + "topics"));
        co.addEntry(new OutlineEntry("topics_my", "", "topics_my", ModuleConst.BASE_URL + "topics/s/my"));

        LinkedList<IOutcomeObject> list = new LinkedList<>();
        list.add(co);

        Outcome outcome = new Outcome();
        outcome.addPayload("nav", list);

        return Response.ok(outcome).build();
    }
}
