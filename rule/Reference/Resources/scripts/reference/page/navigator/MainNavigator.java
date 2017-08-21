package reference.page.navigator;

import com.exponentus.env.AvailableApplicationTypes;
import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;

import java.util.LinkedList;

public class MainNavigator extends _DoPage {

    @Override
    public void doGET(_Session session, WebFormData formData) {
        LinkedList<IOutcomeObject> list = new LinkedList<>();
        Outline common_outline = new Outline(getLocalizedWord("common_reference_data", session.getLang()), "common");
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("countries", session.getLang()), "country-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("regions", session.getLang()), "region-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("districts", session.getLang()), "district-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("localities", session.getLang()), "locality-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("city_districts", session.getLang()), "citydistrict-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("streets", session.getLang()), "street-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("department_types", session.getLang()), "departmenttype-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("org_categories", session.getLang()), "orgcategory-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("region_types", session.getLang()), "regiontype-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("locality_types", session.getLang()), "localitytype-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("positions", session.getLang()), "position-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("doc_languages", session.getLang()), "documentlanguage-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("doc_types", session.getLang()), "documenttype-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("doc_subjs", session.getLang()), "documentsubject-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("tags", session.getLang()), "tag-view"));
        common_outline.addEntry(new OutlineEntry(getLocalizedWord("text_template", session.getLang()), "texttemplate-view"));

        Outline specific_outline = new Outline(getLocalizedWord("specific_reference_data", session.getLang()), "specific");

        if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.semantyca.name())) {
            specific_outline.addEntry(new OutlineEntry(getLocalizedWord("task_types", session.getLang()), "tasktype-view"));
            specific_outline.addEntry(new OutlineEntry(getLocalizedWord("request_types", session.getLang()), "requesttype-view"));
            specific_outline.addEntry(new OutlineEntry(getLocalizedWord("demand_types", session.getLang()), "demandtype-view"));
            specific_outline.addEntry(new OutlineEntry(getLocalizedWord("approval_route", session.getLang()), "approvalroute-view"));
            specific_outline.addEntry(new OutlineEntry(getLocalizedWord("vehicle", session.getLang()), "vehicle-view"));
            specific_outline.addEntry(new OutlineEntry(getLocalizedWord("control_types", session.getLang()), "controltype-view"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase("claimswork")) {
   //         specific_outline.addEntry(new OutlineEntry(getLocalizedWord("claim_decision_type", session.getLang()), "claimdecisiontype-view"));
   //         specific_outline.addEntry(new OutlineEntry(getLocalizedWord("defendant_type", session.getLang()), "defendanttype-view"));
   //         specific_outline.addEntry(new OutlineEntry(getLocalizedWord("dispute_type", session.getLang()), "disputetype-view"));
   //         specific_outline.addEntry(new OutlineEntry(getLocalizedWord("law_article", session.getLang()), "lawarticle-view"));
   //         specific_outline.addEntry(new OutlineEntry(getLocalizedWord("law_branch", session.getLang()), "lawbranch-view"));
   //         specific_outline.addEntry(new OutlineEntry(getLocalizedWord("responsible_type", session.getLang()), "responsibletype-view"));
        }else  if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.landrelationstar.name())) {
            specific_outline.addEntry(new OutlineEntry(getLocalizedWord("property_codes", session.getLang()), "propertycode-view"));
           // specific_outline.addEntry(new OutlineEntry(getLocalizedWord("receiving_reason", session.getLang()), "receivingreason-view"));
           // specific_outline.addEntry(new OutlineEntry(getLocalizedWord("structure_type", session.getLang()), "structuretype-view"));
           // specific_outline.addEntry(new OutlineEntry(getLocalizedWord("building_materials", session.getLang()), "buildingmaterial-view"));
        }

        list.add(common_outline);
        list.add(specific_outline);

        addValue("workspaceUrl", Environment.getWorkspaceURL());
        addValue("outline_current", formData.getValueSilently("id").replace("-form", "-view"));
        addContent(list);
    }
}
