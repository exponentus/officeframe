package staff.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import staff.dao.OrganizationDAO;
import staff.init.ModuleConst;
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
        LinkedList<Outline> list = new LinkedList<>();
        List<OutlineEntry> primaryOrgs = new ArrayList<OutlineEntry>();

        Outline co = new Outline("", "common");
        try {
            OrganizationDAO oDao = new OrganizationDAO(session);
            List<Organization> po = oDao.findPrimaryOrg();
            if (po != null) {
                for (Organization primaryOrg : po) {
                  /*  primaryOrgs.add(new OutlineEntry(primaryOrg.getLocName(lang), primaryOrg.getLocName(lang),
                            "structures", ModuleConst.BASE_URL + "structures"));*/
                }
            }

            for (OutlineEntry entry : primaryOrgs) {
                co.addEntry(entry);
            }
            co.addEntry(new OutlineEntry("organizations", "", "fa fa-building", "organizations", ModuleConst.BASE_URL + "organizations"));
            co.addEntry(new OutlineEntry("departments", "", "fa fa-sitemap", "departments", ModuleConst.BASE_URL + "departments"));
            co.addEntry(new OutlineEntry("employees", "", "fa fa-users", "employees", ModuleConst.BASE_URL + "employees?fired=true"));
            co.addEntry(new OutlineEntry("individuals", "", "fa fa-user-o", "individuals", ModuleConst.BASE_URL + "individuals"));

            Outline ro = new Outline("", "staff_ref");
            ro.addEntry(new OutlineEntry("roles", "", "fa fa-flag", "roles", ModuleConst.BASE_URL + "roles"));
            ro.addEntry(new OutlineEntry("organization_labels", "", "fa fa-tags", "organization_labels", ModuleConst.BASE_URL + "organization-labels"));
            ro.addEntry(new OutlineEntry("individual_labels", "", "fa fa-tags", "individual_labels", ModuleConst.BASE_URL + "individual-labels"));

            list.add(co);
            list.add(ro);

            Outcome outcome = new Outcome();
            outcome.addPayload("nav", list);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return null;
        }
    }
}
