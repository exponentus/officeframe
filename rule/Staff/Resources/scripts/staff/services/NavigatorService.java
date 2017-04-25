package staff.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.outline._Outline;
import com.exponentus.scripting.outline._OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import staff.dao.OrganizationDAO;
import staff.init.AppConst;
import staff.model.Organization;

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
        List<_OutlineEntry> primaryOrgs = new ArrayList<_OutlineEntry>();

        _Outline co = new _Outline("common_staff_data", "common");
        try {
            OrganizationDAO oDao = new OrganizationDAO(session);
            List<Organization> po = oDao.findPrimaryOrg();
            if (po != null) {
                for (Organization primaryOrg : po) {
                    primaryOrgs.add(new _OutlineEntry(primaryOrg.getLocName(lang), primaryOrg.getLocName(lang),
                            "structures", AppConst.BASE_URL + "structures"));
                }
            }

            for (_OutlineEntry entry : primaryOrgs) {
                co.addEntry(entry);
            }
            co.addEntry(new _OutlineEntry("organizations", "", "organizations", AppConst.BASE_URL + "organizations"));
            co.addEntry(new _OutlineEntry("departments", "", "departments", AppConst.BASE_URL + "departments"));
            co.addEntry(new _OutlineEntry("employees", "", "employees", AppConst.BASE_URL + "employees"));
            co.addEntry(new _OutlineEntry("roles", "", "roles", AppConst.BASE_URL + "roles"));
            co.addEntry(new _OutlineEntry("organization_labels", "", "organization_labels", AppConst.BASE_URL + "organization_labels"));

            list.add(co);

            Outcome outcome = new Outcome();
            outcome.addPayload("nav", list);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return null;
        }
    }
}
