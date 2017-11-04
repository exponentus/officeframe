package reference.services;

import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.ActivityTypeCategoryDAO;
import reference.model.ActivityTypeCategory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("activity-type-categories")
public class ActivityTypeCategoryService extends ReferenceService<ActivityTypeCategory> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        Outcome outcome = new Outcome();
        SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
        try {
            ActivityTypeCategoryDAO dao = new ActivityTypeCategoryDAO(session);
            ViewPage<ActivityTypeCategory> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            outcome.addPayload(new ConventionalActionFactory().getViewActionBar(session, true));
            outcome.setTitle("activity_types");
            outcome.addPayload("contentTitle", "activity_types");
            outcome.addPayload(vp);
            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }

    }
}
