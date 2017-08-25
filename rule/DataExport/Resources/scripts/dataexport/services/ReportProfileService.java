package dataexport.services;

import com.exponentus.common.service.EntityService;
import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._ActionBar;
import dataexport.domain.ReportProfileDomain;
import dataexport.model.ReportProfile;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("report-profiles")
@Produces(MediaType.APPLICATION_JSON)
public class ReportProfileService extends EntityService<ReportProfile, ReportProfileDomain> {

    @GET
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();

        int pageSize = session.getPageSize();
        SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));

        ViewPage vp = getDomain().getViewPage(sortParams, params.getPage(), pageSize);

        _ActionBar actionBar = new _ActionBar(session);
        actionBar.addAction(new ConventionalActionFactory().refreshVew);

        Outcome outcome = new Outcome();
        outcome.setId("report-profiles");
        outcome.setTitle("report_profiles");
        outcome.addPayload(actionBar);
        outcome.addPayload(vp);
        return Response.ok(outcome).build();

    }

}
