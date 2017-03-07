package staff.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.ServiceDescriptor;
import com.exponentus.rest.ServiceMethod;
import com.exponentus.rest.outgoingpojo.Outcome;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.outline._Outline;
import com.exponentus.scripting.outline._OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import staff.dao.OrganizationDAO;
import staff.model.Organization;

import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
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
        List<_OutlineEntry> primaryOrgs = new ArrayList<_OutlineEntry>();

        _Outline co = new _Outline("", "common");
        try {
            OrganizationDAO oDao = new OrganizationDAO(session);
            List<Organization> po = oDao.findPrimaryOrg();
            if (po != null) {
                for (Organization primaryOrg : po) {
                    primaryOrgs.add(new _OutlineEntry(primaryOrg.getLocName(lang), primaryOrg.getLocName(lang), "structures", "structures"));
                }
            }

            for (_OutlineEntry entry : primaryOrgs) {
                co.addEntry(entry);
            }
            co.addEntry(new _OutlineEntry("organizations", "organizations"));
            co.addEntry(new _OutlineEntry("departments", "departments"));
            co.addEntry(new _OutlineEntry("employees", "employees"));
            co.addEntry(new _OutlineEntry("roles", "roles"));
            co.addEntry(new _OutlineEntry("organization_labels", "organization_labels"));

            list.add(co);

            Outcome outcome = new Outcome();
            outcome.addPayload("nav", list);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return null;
        }
    }

    @Override
    public ServiceDescriptor updateDescription(ServiceDescriptor sd) {
        sd.setName(getClass().getName());
        ServiceMethod m = new ServiceMethod();
        m.setMethod(HttpMethod.GET);
        m.setURL("/" + sd.getAppName() + sd.getUrlMapping());
        sd.addMethod(m);
        return sd;
    }
}
