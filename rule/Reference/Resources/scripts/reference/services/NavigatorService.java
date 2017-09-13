package reference.services;

import com.exponentus.env.AvailableApplicationTypes;
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
        co.addEntry(new OutlineEntry("doc_subjs", "", "document_subjects", AppConst.BASE_URL + "document_subjects"));
        co.addEntry(new OutlineEntry("tags", "", "tags", AppConst.BASE_URL + "tags"));
        co.addEntry(new OutlineEntry("text_template", "", "text_templates", AppConst.BASE_URL + "text_templates"));

        Outline so = new Outline("specific_reference_data", "specific");

        if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.SEMANTYCA.name())) {
            so.addEntry(new OutlineEntry("control_types", "", "control_types", AppConst.BASE_URL + "control_types"));
            so.addEntry(new OutlineEntry("task_types", "", "task_types", AppConst.BASE_URL + "task_types"));
            so.addEntry(new OutlineEntry("request_types", "", "request_types", AppConst.BASE_URL + "request_types"));
            so.addEntry(new OutlineEntry("demand_types", "", "demand_types", AppConst.BASE_URL + "demand_types"));
            so.addEntry(new OutlineEntry("work_types", "", "work_types", AppConst.BASE_URL + "work_types"));
            so.addEntry(new OutlineEntry("approval_routes", "", "approval_routes", AppConst.BASE_URL + "approval_routes"));
            so.addEntry(new OutlineEntry("vehicle", "", "vehicle", AppConst.BASE_URL + "vehicles"));
            so.addEntry(new OutlineEntry("meeting_room", "", "meeting_rooms", AppConst.BASE_URL + "meeting_rooms"));
            //} else if (EnvConst.APP_ID.equalsIgnoreCase("constructionaudit")) {
            //  so.addEntry(new OutlineEntry("work_types", "", "work_types", AppConst.BASE_URL + "work_types"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.CONTROLPANEL.name())) {
            so.addEntry(new OutlineEntry("industry_type_categories", "", "industry-type-categories", AppConst.BASE_URL + "industry-type-categories"));
            so.addEntry(new OutlineEntry("industry_types", "", "industry-types", AppConst.BASE_URL + "industry-types"));
            so.addEntry(new OutlineEntry("nationalities", "", "nationalities", AppConst.BASE_URL + "nationalities"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.LANDRELATIONSTAR.name())) {
            so.addEntry(new OutlineEntry("property_codes", "", "property_codes", AppConst.BASE_URL + "property_codes"));
            so.addEntry(new OutlineEntry("real_estate_obj_statuses", "", "real_estate_obj_statuses", AppConst.BASE_URL + "real_estate_obj_statuses"));
            //   so.addEntry(new OutlineEntry("structure_type", "", "structure_types", AppConst.BASE_URL + "structure_types"));
            //   so.addEntry(new OutlineEntry("building_materials", "", "building_materials", AppConst.BASE_URL + "building_materials"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.ARCHITECTURETAR.name())) {
            so.addEntry(new OutlineEntry("building_states", "", "building_states", AppConst.BASE_URL + "building_states"));
            so.addEntry(new OutlineEntry("building_materials", "", "building_materials", AppConst.BASE_URL + "building_materials"));
        }

        list.add(co);
        if (!so.getEntries().isEmpty()) {
            list.add(so);
        }

        Outcome outcome = new Outcome();
        outcome.addPayload("nav", list);

        return Response.ok(outcome).build();
    }
}
