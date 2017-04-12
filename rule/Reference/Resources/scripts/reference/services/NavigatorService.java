package reference.services;

import com.exponentus.env.EnvConst;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.outline._Outline;
import com.exponentus.scripting.outline._OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.LinkedList;

@Path("navigator")
public class NavigatorService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNav() {
        Collection<IOutcomeObject> list = new LinkedList<>();

        _Outline co = new _Outline("common_reference_data", "common");

        co.addEntry(new _OutlineEntry("countries", "", "countries", "countries"));
        co.addEntry(new _OutlineEntry("regions", "", "regions", "regions"));
        co.addEntry(new _OutlineEntry("districts", "", "districts", "districts"));
        co.addEntry(new _OutlineEntry("localities", "", "localities", "localities"));
        co.addEntry(new _OutlineEntry("city_districts", "", "city_districts", "city_districts"));
        co.addEntry(new _OutlineEntry("streets", "", "streets", "streets"));
        co.addEntry(new _OutlineEntry("department_types", "", "department_types", "department_types"));
        co.addEntry(new _OutlineEntry("org_categories", "", "org_categories", "org_categories"));
        co.addEntry(new _OutlineEntry("region_types", "", "region_types", "region_types"));
        co.addEntry(new _OutlineEntry("locality_types", "", "locality_types", "locality_types"));
        co.addEntry(new _OutlineEntry("positions", "", "positions", "positions"));
        co.addEntry(new _OutlineEntry("doc_languages", "", "document_languages", "document_languages"));
        co.addEntry(new _OutlineEntry("doc_types", "", "document_types", "document_types"));
        co.addEntry(new _OutlineEntry("doc_subjects", "", "document_subjects", "document_subjects"));
        co.addEntry(new _OutlineEntry("control_types", "", "control_types", "control_types"));
        co.addEntry(new _OutlineEntry("tags", "", "tags", "tags"));
        co.addEntry(new _OutlineEntry("text_template", "", "text_templates", "text_templates"));

        _Outline so = new _Outline("specific_reference_data", "specific");

        if (EnvConst.APP_ID.equalsIgnoreCase("comproperty")) {
            so.addEntry(new _OutlineEntry("kuf", "", "kuf", "kuf"));
            so.addEntry(new _OutlineEntry("property_codes", "", "property_codes", "property_codes"));
            so.addEntry(new _OutlineEntry("receiving_reason", "", "receiving_reasons", "receiving_reasons"));
            so.addEntry(new _OutlineEntry("structure_type", "", "structure_types", "structure_types"));
            so.addEntry(new _OutlineEntry("building_materials", "", "building_materials", "building_materials"));
            // TODO it is weird code
        } else if (EnvConst.APP_ID.equalsIgnoreCase("poema") || EnvConst.APP_ID.equalsIgnoreCase("run")) {
            so.addEntry(new _OutlineEntry("task_types", "", "task_types", "task_types"));
            so.addEntry(new _OutlineEntry("request_types", "", "request_types", "request_types"));
            so.addEntry(new _OutlineEntry("demand_types", "", "demand_types", "demand_types"));
            so.addEntry(new _OutlineEntry("work_types", "", "work_types", "work_types"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase("claimswork")) {
            so.addEntry(new _OutlineEntry("claimant_decision_types", "", "claimant_decision_types", "claimant_decision_types"));
            so.addEntry(new _OutlineEntry("defendant_type", "", "defendant_types", "defendant_types"));
            so.addEntry(new _OutlineEntry("dispute_type", "", "dispute_types", "dispute_types"));
            so.addEntry(new _OutlineEntry("law_article", "", "law_articles", "law_articles"));
            so.addEntry(new _OutlineEntry("law_branch", "", "law_branches", "law_branches"));
            so.addEntry(new _OutlineEntry("responsible_type", "", "responsible_types", "responsible_types"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase("constructionaudit")) {
            so.addEntry(new _OutlineEntry("work_types", "", "work_types", "work_types"));
        }

        so.addEntry(new _OutlineEntry("vehicle", "", "vehicle", "vehicles"));

        list.add(co);
        list.add(so);

        Outcome outcome = new Outcome();
        outcome.addPayload("nav", list);

        return Response.ok(outcome).build();
    }
}
