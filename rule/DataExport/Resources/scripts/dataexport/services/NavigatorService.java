package dataexport.services;

import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import dataexport.init.AppConst;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Path("navigator")
public class NavigatorService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNav() {
        _Session session = getSession();

        LanguageCode lang = session.getLang();
        LinkedList<IOutcomeObject> list = new LinkedList<>();
        List<OutlineEntry> primaryOrgs = new ArrayList<OutlineEntry>();

        Outline co = new Outline("common_data", "common");

            co.addEntry(new OutlineEntry("export_profiles", "", "export_profiles", AppConst.BASE_URL + "export_profiles"));

            list.add(co);

            Outcome outcome = new Outcome();
            outcome.addPayload("nav", list);

            return Response.ok(outcome).build();

    }
}
