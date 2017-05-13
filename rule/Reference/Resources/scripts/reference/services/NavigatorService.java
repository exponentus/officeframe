package reference.services;

import com.exponentus.env.EnvConst;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import reference.init.AppConst;

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

        Outline co = new Outline("common_reference_data", "common");

        co.addEntry(new OutlineEntry("countries", "", "countries", AppConst.BASE_URL + "countries"));
        co.addEntry(new OutlineEntry("regions", "", "regions", AppConst.BASE_URL + "regions"));
        co.addEntry(new OutlineEntry("districts", "", "districts", AppConst.BASE_URL + "districts"));
        co.addEntry(new OutlineEntry("localities", "", "localities", AppConst.BASE_URL + "localities"));
        co.addEntry(new OutlineEntry("city_districts", "", "city_districts", AppConst.BASE_URL + "city_districts"));
        co.addEntry(new OutlineEntry("streets", "", "streets", AppConst.BASE_URL + "streets"));
        co.addEntry(new OutlineEntry("department_types", "", "department_types", AppConst.BASE_URL + "department_types"));
        co.addEntry(new OutlineEntry("org_categories", "", "org_categories", AppConst.BASE_URL + "org_categories"));
        co.addEntry(new OutlineEntry("region_types", "", "region_types", AppConst.BASE_URL + "region_types"));
        co.addEntry(new OutlineEntry("locality_types", "", "locality_types", AppConst.BASE_URL + "locality_types"));
        co.addEntry(new OutlineEntry("positions", "", "positions", AppConst.BASE_URL + "positions"));
        co.addEntry(new OutlineEntry("doc_languages", "", "document_languages", AppConst.BASE_URL + "document_languages"));
        co.addEntry(new OutlineEntry("doc_types", "", "document_types", AppConst.BASE_URL + "document_types"));
        co.addEntry(new OutlineEntry("doc_subjects", "", "document_subjects", AppConst.BASE_URL + "document_subjects"));
        co.addEntry(new OutlineEntry("control_types", "", "control_types", AppConst.BASE_URL + "control_types"));
        co.addEntry(new OutlineEntry("tags", "", "tags", AppConst.BASE_URL + "tags"));
        co.addEntry(new OutlineEntry("text_template", "", "text_templates", AppConst.BASE_URL + "text_templates"));

        Outline so = new Outline("specific_reference_data", "specific");

        if (EnvConst.APP_ID.equalsIgnoreCase("comproperty")) {
            so.addEntry(new OutlineEntry("kuf", "", "kuf", AppConst.BASE_URL + "kuf"));
            so.addEntry(new OutlineEntry("property_codes", "", "property_codes", AppConst.BASE_URL + "property_codes"));
            so.addEntry(new OutlineEntry("receiving_reason", "", "receiving_reasons", AppConst.BASE_URL + "receiving_reasons"));
            so.addEntry(new OutlineEntry("structure_type", "", "structure_types", AppConst.BASE_URL + "structure_types"));
            so.addEntry(new OutlineEntry("building_materials", "", "building_materials", AppConst.BASE_URL + "building_materials"));
            // TODO it is weird code
        } else if (EnvConst.APP_ID.equalsIgnoreCase("poema") || EnvConst.APP_ID.equalsIgnoreCase("run")) {
            so.addEntry(new OutlineEntry("task_types", "", "task_types", AppConst.BASE_URL + "task_types"));
            so.addEntry(new OutlineEntry("request_types", "", "request_types", AppConst.BASE_URL + "request_types"));
            so.addEntry(new OutlineEntry("demand_types", "", "demand_types", AppConst.BASE_URL + "demand_types"));
            so.addEntry(new OutlineEntry("work_types", "", "work_types", AppConst.BASE_URL + "work_types"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase("claimswork")) {
            so.addEntry(new OutlineEntry("claimant_decision_types", "", "claimant_decision_types", AppConst.BASE_URL + "claimant_decision_types"));
            so.addEntry(new OutlineEntry("defendant_type", "", "defendant_types", AppConst.BASE_URL + "defendant_types"));
            so.addEntry(new OutlineEntry("dispute_type", "", "dispute_types", AppConst.BASE_URL + "dispute_types"));
            so.addEntry(new OutlineEntry("law_article", "", "law_articles", AppConst.BASE_URL + "law_articles"));
            so.addEntry(new OutlineEntry("law_branch", "", "law_branches", AppConst.BASE_URL + "law_branches"));
            so.addEntry(new OutlineEntry("responsible_type", "", "responsible_types", AppConst.BASE_URL + "responsible_types"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase("constructionaudit")) {
            so.addEntry(new OutlineEntry("work_types", "", "work_types", AppConst.BASE_URL + "work_types"));
        }

        so.addEntry(new OutlineEntry("vehicle", "", "vehicle", AppConst.BASE_URL + "vehicles"));
        so.addEntry(new OutlineEntry("meeting_rooms", "", "meeting_rooms", AppConst.BASE_URL + "meeting_rooms"));

        list.add(co);
        list.add(so);

        Outcome outcome = new Outcome();
        outcome.addPayload("nav", list);

        return Response.ok(outcome).build();
    }
}
