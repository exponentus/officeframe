package reference.page.navigator;

import java.util.LinkedList;

import com.exponentus.env.EnvConst;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._WebFormData;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.scripting.outline._Outline;
import com.exponentus.scripting.outline._OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;

public class MainNavigator extends _DoPage {

	@Override
	public void doGET(_Session session, _WebFormData formData) {
		LinkedList<IOutcomeObject> list = new LinkedList<IOutcomeObject>();

		_Outline common_outline = new _Outline(getLocalizedWord("common_reference_data", session.getLang()), "common");

		// if(cuser.hasRole(["struct_keeper", "supervisor"])) {
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("countries", session.getLang()), "country-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("regions", session.getLang()), "region-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("districts", session.getLang()), "district-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("localities", session.getLang()), "locality-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("city_districts", session.getLang()), "citydistrict-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("streets", session.getLang()), "street-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("department_types", session.getLang()), "departmenttype-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("org_categories", session.getLang()), "orgcategory-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("region_types", session.getLang()), "regiontype-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("locality_types", session.getLang()), "localitytype-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("positions", session.getLang()), "position-view"));
		common_outline.addEntry(new _OutlineEntry(getLocalizedWord("tags", session.getLang()), "tag-view"));

		_Outline specific_outline = new _Outline(getLocalizedWord("specific_reference_data", session.getLang()), "specific");
		if (EnvConst.APP_ID.equalsIgnoreCase("comproperty")) {
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("kuf", session.getLang()), "kuf-view"));
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("property_codes", session.getLang()), "propertycode-view"));
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("receiving_reason", session.getLang()), "receivingreason-view"));
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("structure_type", session.getLang()), "structuretype-view"));
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("building_materials", session.getLang()), "buildingmaterial-view"));
		} else if (EnvConst.APP_ID.equalsIgnoreCase("poema")) {
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("task_types", session.getLang()), "tasktype-view"));
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("request_types", session.getLang()), "requesttype-view"));
		} else if (EnvConst.APP_ID.equalsIgnoreCase("claimswork")) {
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("claim_decision_type", session.getLang()), "claimdecisiontype-view"));
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("defendant_type", session.getLang()), "defendanttype-view"));
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("dispute_type", session.getLang()), "disputetype-view"));
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("law_article", session.getLang()), "lawarticle-view"));
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("law_branch", session.getLang()), "lawbranch-view"));
			specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("responsible_type", session.getLang()), "responsibletype-view"));
		}

		list.add(common_outline);
		list.add(specific_outline);

		addValue("outline_current", formData.getValueSilently("id").replace("-form", "-view"));
		addContent(list);
	}
}
