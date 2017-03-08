package staff.page.navigator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.Environment;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.scripting.outline._Outline;
import com.exponentus.scripting.outline._OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;

import staff.dao.OrganizationDAO;
import staff.dao.OrganizationLabelDAO;
import staff.dao.RoleDAO;
import staff.model.Organization;
import staff.model.OrganizationLabel;
import staff.model.Role;

public class MainNavigator extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		LanguageCode lang = session.getLang();
		LinkedList<IOutcomeObject> list = new LinkedList<>();
		List<_OutlineEntry> primaryOrgs = new ArrayList<_OutlineEntry>();

		_Outline common_outline = new _Outline(getLocalizedWord("common_staff_data", lang), "common");
		try {
			OrganizationDAO oDao;
			oDao = new OrganizationDAO(session);
			List<Organization> po = oDao.findPrimaryOrg();
			if (po != null) {
				for (Organization primaryOrg : po) {
					primaryOrgs.add(new _OutlineEntry(primaryOrg.getLocName(lang),
							getLocalizedWord("primary_organization", lang) + " : " + primaryOrg.getLocName(lang),
							"structure-view", "p?id=structure-view"));
				}
			}
			_OutlineEntry orgEntry = new _OutlineEntry(getLocalizedWord("organizations", lang), "organization-view");
			for (OrganizationLabel label : new OrganizationLabelDAO(session).findAll().getResult()) {
				orgEntry.addEntry(new _OutlineEntry(label.getLocName(lang),
						getLocalizedWord("labeled", lang) + " : " + label.getLocName(lang),
						"organization-label-view" + label.getId(),
						"p?id=organization-label-view&categoryid=" + label.getId()));
			}
			_OutlineEntry departmentEntry = new _OutlineEntry(getLocalizedWord("departments", lang), "department-view");
			_OutlineEntry employeeEntry = new _OutlineEntry(getLocalizedWord("employees", lang), "employee-view");
			for (Role role : new RoleDAO(session).findAll().getResult()) {
				employeeEntry.addEntry(new _OutlineEntry(role.getLocName(lang),
						getLocalizedWord("assigned", lang) + " : " + role.getLocName(lang), "role-view" + role.getId(),
						"p?id=role-view&categoryid=" + role.getId()));
			}
			
			for (_OutlineEntry entry : primaryOrgs) {
				common_outline.addEntry(entry);
			}
			common_outline.addEntry(orgEntry);
			common_outline.addEntry(departmentEntry);
			common_outline.addEntry(employeeEntry);
			
			common_outline.addEntry(new _OutlineEntry(getLocalizedWord("roles", lang), "role-view"));
			common_outline.addEntry(
					new _OutlineEntry(getLocalizedWord("organization_labels", lang), "organization-label-view"));
			
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
