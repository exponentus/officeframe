package integration.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import integration.dao.ServiceDAO;
import integration.model.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("services")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceService extends RestProvider {


    @GET
    public Response getViewPage() {
        _Session session = getSession();
        IUser user = session.getUser();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();


        Outcome outcome = new Outcome();

        SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
        ServiceDAO dao = new ServiceDAO(session);
        ViewPage<Service> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

        outcome.setTitle("services");
        outcome.addPayload("contentTitle", "services");
        outcome.addPayload(vp);

        return Response.ok(outcome).build();

    }
}
