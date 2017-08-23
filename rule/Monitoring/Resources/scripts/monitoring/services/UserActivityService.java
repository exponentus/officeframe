package monitoring.services;

import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._ActionBar;
import monitoring.dao.UserActivityDAO;
import monitoring.dao.filter.UserActivityFilter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user-activities")
@Produces(MediaType.APPLICATION_JSON)
public class UserActivityService extends RestProvider {
    private ConventionalActionFactory action = new ConventionalActionFactory();

    @GET
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();

        try {
            UserActivityDAO dao = new UserActivityDAO(session);
            int pageSize = session.getPageSize();
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            UserActivityFilter filter = new UserActivityFilter(params);
            ViewPage vp = dao.findViewPage(filter, sortParams, params.getPage(), pageSize);

            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(action.refreshVew);

            Outcome outcome = new Outcome();
            outcome.setId("user-activity");
            outcome.setTitle("user_activity_plural");
            outcome.addPayload(actionBar);
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }

    }
}
