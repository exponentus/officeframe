package staff.page.navigator;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.Environment;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import staff.dao.OrganizationDAO;
import staff.dao.OrganizationLabelDAO;
import staff.dao.RoleDAO;
import staff.model.Organization;
import staff.model.OrganizationLabel;
import staff.model.Role;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainNavigator extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		LanguageCode lang = session.getLang();
		LinkedList<IOutcomeObject> list = new LinkedList<>();
		List<OutlineEntry> primaryOrgs = new ArrayList<OutlineEntry>();

		Outline common_outline = new Outline(getLocalizedWord("common_staff_data", lang), "common");
		try {
			OrganizationDAO oDao;
			oDao = new OrganizationDAO(session);
			List<Organization> po = oDao.findPrimaryOrg();
			if (po != null) {
				for (Organization primaryOrg : po) {
					primaryOrgs.add(new OutlineEntry(primaryOrg.getLocName(lang),
							getLocalizedWord("primary_organization", lang) + " : " + primaryOrg.getLocName(lang),
							"structure-view", "p?id=structure-view"));
				}
			}
			OutlineEntry orgEntry = new OutlineEntry(getLocalizedWord("organizations", lang), "organization-view");
			for (OrganizationLabel label : new OrganizationLabelDAO(session).findAll().getResult()) {
				orgEntry.addEntry(new OutlineEntry(label.getLocName(lang),
						getLocalizedWord("labeled", lang) + " : " + label.getLocName(lang),
						"organization-label-view" + label.getId(),
						"p?id=organization-label-view&categoryid=" + label.getId()));
			}
			OutlineEntry departmentEntry = new OutlineEntry(getLocalizedWord("departments", lang), "department-view");
			OutlineEntry employeeEntry = new OutlineEntry(getLocalizedWord("employees", lang), "employee-view");
			for (Role role : new RoleDAO(session).findAll().getResult()) {
				employeeEntry.addEntry(new OutlineEntry(role.getLocName(lang),
						getLocalizedWord("assigned", lang) + " : " + role.getLocName(lang), "role-view" + role.getId(),
						"p?id=role-view&categoryid=" + role.getId()));
			}
			
			for (OutlineEntry entry : primaryOrgs) {
				common_outline.addEntry(entry);
			}
			common_outline.addEntry(orgEntry);
			common_outline.addEntry(departmentEntry);
			common_outline.addEntry(employeeEntry);
			
			common_outline.addEntry(new OutlineEntry(getLocalizedWord("roles", lang), "role-view"));
			common_outline.addEntry(
					new OutlineEntry(getLocalizedWord("organization_labels", lang), "organization-label-view"));

			
			list.add(common_outline);
			
			addValue("workspaceUrl", Environment.getWorkspaceURL());
			addValue("outline_current", formData.getValueSilently("id").replace("-form", "-view")
					+ formData.getValueSilently("categoryid"));
			addContent(list);
		} catch (DAOException e) {
			logError(e);
			setBadRequest();
			return;
		}

	}
}
