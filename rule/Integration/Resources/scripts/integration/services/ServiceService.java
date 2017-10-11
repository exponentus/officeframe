package integration.services;

import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.view.ColumnOption;
import com.exponentus.common.ui.view.ColumnOptionGroup;
import com.exponentus.common.ui.view.ViewPageOptions;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._ActionBar;
import integration.dao.ServiceDAO;
import integration.model.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("services")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceService extends RestProvider {

    @GET
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
        ServiceDAO dao = new ServiceDAO(session);
        ViewPage<Service> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
        vp.setViewPageOptions(getViewPageOptions());

        ConventionalActionFactory actionFactory = new ConventionalActionFactory();
        _ActionBar actionBar = new _ActionBar(session);
        actionBar.addAction(actionFactory.refreshVew);

        Outcome outcome = new Outcome();
        outcome.setTitle("services");
        outcome.addPayload("contentTitle", "services");
        outcome.addPayload(vp);
        outcome.addPayload(actionBar);

        return Response.ok(outcome).build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") String id) {
        _Session session = getSession();
        ServiceDAO dao = new ServiceDAO(session);
        Service service = dao.findByClassName(id);

        Outcome outcome = new Outcome();
        outcome.setTitle("service");
        outcome.addPayload("contentTitle", "service");
        outcome.addPayload(service);

        return Response.ok(outcome).build();
    }

    private ViewPageOptions getViewPageOptions() {
        ViewPageOptions result = new ViewPageOptions();
        ColumnOptionGroup cg = new ColumnOptionGroup();

        cg.add(new ColumnOption("descr.appName").name("app_name"));
        cg.add(new ColumnOption("descr.clazz").name("clazz"));
        cg.add(new ColumnOption("descr.status").name("status"));
        cg.add(new ColumnOption("descr.url").name("url"));

        List<ColumnOptionGroup> list = new ArrayList<>();
        list.add(cg);

        result.addOption("root", list);
        return result;
    }
}
