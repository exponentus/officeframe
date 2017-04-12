package reference.page.navigator;

import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.scripting.outline._Outline;
import com.exponentus.scripting.outline._OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;

import java.util.LinkedList;

public class MainNavigator extends _DoPage {

    @Override
    public void doGET(_Session session, WebFormData formData) {
        LinkedList<IOutcomeObject> list = new LinkedList<>();

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
        common_outline.addEntry(new _OutlineEntry(getLocalizedWord("doc_languages", session.getLang()), "documentlanguage-view"));
        common_outline.addEntry(new _OutlineEntry(getLocalizedWord("doc_types", session.getLang()), "documenttype-view"));
        common_outline.addEntry(new _OutlineEntry(getLocalizedWord("doc_subjs", session.getLang()), "documentsubject-view"));
        common_outline.addEntry(new _OutlineEntry(getLocalizedWord("control_types", session.getLang()), "controltype-view"));
        common_outline.addEntry(new _OutlineEntry(getLocalizedWord("tags", session.getLang()), "tag-view"));
        common_outline.addEntry(new _OutlineEntry(getLocalizedWord("text_template", session.getLang()), "texttemplate-view"));
        common_outline.addEntry(new _OutlineEntry(getLocalizedWord("vehicle", session.getLang()), "vehicle-view"));

        _Outline specific_outline = new _Outline(getLocalizedWord("specific_reference_data", session.getLang()), "specific");

        if (EnvConst.APP_ID.equalsIgnoreCase("comproperty")) {
            specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("kuf", session.getLang()), "kuf-view"));
            specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("property_codes", session.getLang()), "propertycode-view"));
            specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("receiving_reason", session.getLang()), "receivingreason-view"));
            specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("structure_type", session.getLang()), "structuretype-view"));
            specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("building_materials", session.getLang()), "buildingmaterial-view"));
            // TODO it is weird code
        } else if (EnvConst.APP_ID.equalsIgnoreCase("poema") || EnvConst.APP_ID.equalsIgnoreCase("run")) {
            specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("task_types", session.getLang()), "tasktype-view"));
            specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("request_types", session.getLang()), "requesttype-view"));
            specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("demand_types", session.getLang()), "demandtype-view"));
            specific_outline.addEntry(new _OutlineEntry(getLocalizedWord("approval_route", session.getLang()), "approvalroute-view"));
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

        addValue("workspaceUrl", Environment.getWorkspaceURL());
        addValue("outline_current", formData.getValueSilently("id").replace("-form", "-view"));
        addContent(list);
    }
}
