package monitoring.services;

import administrator.dao.ModuleDAO;
import administrator.dao.UserDAO;
import administrator.model.Application;
import com.exponentus.common.dao.DAOFactory;
import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.actions.ActionBar;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.env.EnvConst;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import com.exponentus.util.ReflectionUtil;
import monitoring.dao.UserActivityDAO;
import monitoring.dto.converter.UserActivityDtoConverter;
import monitoring.model.UserActivity;
import monitoring.ui.ViewOptions;
import staff.dao.EmployeeDAO;
import staff.model.Employee;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Stream;

@Path("user-activities")
@Produces(MediaType.APPLICATION_JSON)
public class UserActivityService extends RestProvider {

    private ConventionalActionFactory action = new ConventionalActionFactory();

    @GET
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();

        UserActivityDAO dao = new UserActivityDAO(session);
        int pageSize = session.getPageSize();
        ViewPage<UserActivity> vp = dao.findAll(params.getPage(), pageSize);
        vp.setResult(new UserActivityDtoConverter().convert(vp.getResult()));
        vp.setViewPageOptions(new ViewOptions().getUserActivityOptions());

        ActionBar actionBar = new ActionBar(session);
        actionBar.addAction(action.refreshVew);

        Outcome outcome = new Outcome();
        outcome.setId("user-activity");
        outcome.setTitle("user_activities");
        outcome.setPayloadTitle("user_activities");
        outcome.addPayload(actionBar);
        outcome.addPayload(vp);

        return Response.ok(outcome).build();
    }

    @GET
    @Path("last-visits")
    public Response getLastLoginViewPage() {
        _Session ses = getSession();

        UserActivityDAO dao = new UserActivityDAO(ses);
        ViewPage<UserActivity> vp = dao.getLastVisits(getWebFormData().getPage(), ses.getPageSize());
        vp.setResult(new UserActivityDtoConverter().convert(vp.getResult()));
        vp.setViewPageOptions(new ViewOptions().getLastVisitOptions());

        ActionBar actionBar = new ActionBar(ses);
        actionBar.addAction(action.refreshVew);

        Outcome outcome = new Outcome();
        outcome.setId("last-visits");
        outcome.setTitle("last_logins");
        outcome.setPayloadTitle("last_logins");
        outcome.addPayload(actionBar);
        outcome.addPayload(vp);

        return Response.ok(outcome).build();
    }

    @GET
    @Path("count-of-records")
    public Response getRecordsCount() {
        _Session ses = getSession();
        try {
            UserActivityDAO dao = new UserActivityDAO(ses);

            ActionBar actionBar = new ActionBar(ses);
            actionBar.addAction(action.refreshVew);

            Outcome outcome = new Outcome();
            outcome.setId("count-of-records");
            outcome.setTitle("count_of_records");
            outcome.setPayloadTitle("count_of_records");
            outcome.addPayload(actionBar);

            List result = new ArrayList();
            ModuleDAO applicationDAO = new ModuleDAO(ses);
            UserDAO userDAO = new UserDAO(ses);
            EmployeeDAO employeeDAO = new EmployeeDAO(ses);
            for (IUser user : userDAO.findAll()) {
                Map<String, Object> resultRow = new HashMap<>();
                Employee uemp = employeeDAO.findByUser(user);
                resultRow.put("actUser", (uemp == null ? user.getLogin() : uemp.getName()));
                resultRow.put("kind", "userActivity");
                resultRow.put("id", user.getId());

                int totalCount = 0;
                for (Application application : applicationDAO.findAllActivated()) {
                    if (!Stream.of(EnvConst.OFFICEFRAME_APPLICATION_MODULES).anyMatch(x -> x.equals(application.getName()))
                            && !application.getName().equals(EnvConst.ADMINISTRATOR_MODULE_NAME)) {
                        for (Class<IAppEntity<UUID>> clazz : ReflectionUtil.getAllAppEntities(application.getName().toLowerCase() + ".model")) {
                            IDAO idao = DAOFactory.get(ses, clazz);
                            if (idao != null) {
                                ViewPage viewPage = idao.findAllEqual("author", user, 0, 0);
                                if (viewPage != null) {
                                    totalCount += viewPage.getCount();
                                }
                            }
                        }

                    }
                }
                resultRow.put("count", totalCount);
                result.add(resultRow);
            }

            ViewPage vp = new ViewPage(result, result.size(), 1, 1);
            vp.setViewPageOptions(new ViewOptions().getCountOfRecordOptions());

            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }
}
