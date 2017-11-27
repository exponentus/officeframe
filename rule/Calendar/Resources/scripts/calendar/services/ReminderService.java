package calendar.services;

import calendar.dao.ReminderDAO;
import calendar.domain.ReminderDomain;
import calendar.model.Reminder;
import com.exponentus.common.service.EntityService;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.view.ViewColumn;
import com.exponentus.common.ui.view.ViewColumnGroup;
import com.exponentus.common.ui.view.ViewColumnType;
import com.exponentus.common.ui.view.ViewPageOptions;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("reminders")
@Produces(MediaType.APPLICATION_JSON)
public class ReminderService extends EntityService<Reminder, ReminderDomain> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            ReminderDAO dao = new ReminderDAO(session);
            ViewPage<Reminder> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

            ViewPageOptions vo = new ViewPageOptions();
            ViewColumnGroup cg = new ViewColumnGroup();
            cg.add(new ViewColumn("reminderType").type(ViewColumnType.translate));
            cg.add(new ViewColumn("description"));
            List<ViewColumnGroup> list = new ArrayList<>();
            list.add(cg);
            vo.setRoot(list);
            vp.setViewPageOptions(vo);

            Outcome outcome = new Outcome();
            outcome.setTitle("reminders");
            outcome.addPayload("contentTitle", "reminders");
            outcome.addPayload(vp);
            outcome.addPayload(getDefaultViewActionBar(true));

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }
}
