package staff.page.navigator;

import java.util.LinkedList;

import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.scripting.outline._Outline;
import com.exponentus.scripting.outline._OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;

import staff.dao.OrganizationLabelDAO;
import staff.dao.RoleDAO;
import staff.model.OrganizationLabel;
import staff.model.Role;

public class MainNavigator extends _DoPage {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		LanguageCode lang = session.getLang();
		LinkedList<IOutcomeObject> list = new LinkedList<IOutcomeObject>();

		_Outline common_outline = new _Outline(getLocalizedWord("common_staff_data", lang), "common");
		// common_outline.addEntry(new
		// _OutlineEntry(getLocalizedWord("structure", lang),
		// "structure-view"));
		_OutlineEntry orgEntry = new _OutlineEntry(getLocalizedWord("organizations", lang), "organization-view");
		for (OrganizationLabel label : new OrganizationLabelDAO(session).findAll()) {
			orgEntry.addEntry(
			        new _OutlineEntry(label.getLocalizedName(lang), getLocalizedWord("labeled", lang) + " : " + label.getLocalizedName(lang),
			                "organization-label-view" + label.getId(), "p?id=organization-label-view&categoryid=" + label.getId()));
		}
		_OutlineEntry departmentEntry = new _OutlineEntry(getLocalizedWord("departments", lang), "department-view");
		_OutlineEntry employeeEntry = new _OutlineEntry(getLocalizedWord("employees", lang), "employee-view");
		for (Role role : new RoleDAO(session).findAll()) {
			employeeEntry
			        .addEntry(new _OutlineEntry(role.getLocalizedName(lang), getLocalizedWord("assigned", lang) + " : " + role.getLocalizedName(lang),
			                "role-view" + role.getId(), "p?id=role-view&categoryid=" + role.getId()));
		}

		common_outline.addEntry(orgEntry);
		common_outline.addEntry(departmentEntry);
		common_outline.addEntry(employeeEntry);

		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("roles", lang), "role-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("organization_labels", lang), "organization-label-view"));

		list.add(common_outline);

		addValue("outline_current", formData.getValueSilently("id").replace("-form", "-view") + formData.getValueSilently("categoryid"));
		addContent(list);
	}
}
