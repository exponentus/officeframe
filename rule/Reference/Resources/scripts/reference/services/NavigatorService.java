package reference.services;

import com.exponentus.env.AvailableApplicationTypes;
import com.exponentus.env.EnvConst;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;
import reference.init.ModuleConst;

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
        co.addEntry(new OutlineEntry("countries", "", "countries", ModuleConst.BASE_URL + "countries"));
        co.addEntry(new OutlineEntry("regions", "", "regions", ModuleConst.BASE_URL + "regions"));
        co.addEntry(new OutlineEntry("districts", "", "districts", ModuleConst.BASE_URL + "districts"));
        co.addEntry(new OutlineEntry("localities", "", "localities", ModuleConst.BASE_URL + "localities"));
        co.addEntry(new OutlineEntry("city_districts", "", "city_districts", ModuleConst.BASE_URL + "city-districts"));
        co.addEntry(new OutlineEntry("streets", "", "streets", ModuleConst.BASE_URL + "streets"));
        co.addEntry(new OutlineEntry("department_types", "", "department_types", ModuleConst.BASE_URL + "department-types"));
        co.addEntry(new OutlineEntry("org_categories", "", "org_categories", ModuleConst.BASE_URL + "org-categories"));
        co.addEntry(new OutlineEntry("region_types", "", "region_types", ModuleConst.BASE_URL + "region-types"));
        co.addEntry(new OutlineEntry("locality_types", "", "locality_types", ModuleConst.BASE_URL + "locality-types"));
        co.addEntry(new OutlineEntry("positions", "", "positions", ModuleConst.BASE_URL + "positions"));
        co.addEntry(new OutlineEntry("doc_languages", "", "document_languages", ModuleConst.BASE_URL + "document-languages"));
        co.addEntry(new OutlineEntry("tags", "", "tags", ModuleConst.BASE_URL + "tags"));
        co.addEntry(new OutlineEntry("unit_types", "", "unit_types", ModuleConst.BASE_URL + "unit-types"));
        co.addEntry(new OutlineEntry("approval_routes", "", "approval_routes", ModuleConst.BASE_URL + "approval-routes"));
        Outline so = new Outline("specific_reference_data", "specific");
        if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.SEMANTYCA.name())) {
            so.addEntry(new OutlineEntry("task_types", "", "task_types", ModuleConst.BASE_URL + "task-types"));
            so.addEntry(new OutlineEntry("request_types", "", "request_types", ModuleConst.BASE_URL + "request-types"));
            so.addEntry(new OutlineEntry("demand_types", "", "demand_types", ModuleConst.BASE_URL + "demand-types"));
            so.addEntry(new OutlineEntry("work_types", "", "work_types", ModuleConst.BASE_URL + "work-types"));
            so.addEntry(new OutlineEntry("doc_types", "", "document_types", ModuleConst.BASE_URL + "document-types"));
            so.addEntry(new OutlineEntry("doc_subjs", "", "document_subjects", ModuleConst.BASE_URL + "document-subjects"));
            so.addEntry(new OutlineEntry("text_template", "", "text_templates", ModuleConst.BASE_URL + "text-templates"));
            so.addEntry(new OutlineEntry("vehicle", "", "vehicle", ModuleConst.BASE_URL + "vehicles"));
            so.addEntry(new OutlineEntry("meeting_room", "", "meeting_rooms", ModuleConst.BASE_URL + "meeting-rooms"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.CONTROLPANEL.name())) {
            so.addEntry(new OutlineEntry("activity_types", "", "activity_types", ModuleConst.BASE_URL + "activity-type-categories"));
            so.addEntry(new OutlineEntry("industry_types", "", "industry-types", ModuleConst.BASE_URL + "industry-types"));
            so.addEntry(new OutlineEntry("nationalities", "", "nationalities", ModuleConst.BASE_URL + "nationalities"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.LANDRELATIONSTAR.name())) {
            so.addEntry(new OutlineEntry("property_codes", "", "property_codes", ModuleConst.BASE_URL + "property-codes"));
            so.addEntry(new OutlineEntry("real_estate_obj_statuses", "", "real_estate_obj_statuses", ModuleConst.BASE_URL + "real-estate-obj-statuses"));
            so.addEntry(new OutlineEntry("land_classifications", "", "land_classifications", ModuleConst.BASE_URL + "land-classifications"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.ARCHITECTURETAR.name())) {
            so.addEntry(new OutlineEntry("building_states", "", "building_states", ModuleConst.BASE_URL + "building-states"));
            so.addEntry(new OutlineEntry("building_materials", "", "building_materials", ModuleConst.BASE_URL + "building-materials"));
            so.addEntry(new OutlineEntry("memorial_types", "", "memorial_types", ModuleConst.BASE_URL + "memorial-types"));
            so.addEntry(new OutlineEntry("realestate_obj_purposes", "", "realestate_obj_purposes", ModuleConst.BASE_URL + "realestate-obj-purposes"));
            so.addEntry(new OutlineEntry("land_classifications", "", "land_classifications", ModuleConst.BASE_URL + "land-classifications"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.REGIONECONOMY.name())) {
            so.addEntry(new OutlineEntry("revenue_categories", "", "revenue_categories", ModuleConst.BASE_URL + "revenue-categories"));
            so.addEntry(new OutlineEntry("revenues", "", "revenues", ModuleConst.BASE_URL + "revenues"));
            so.addEntry(new OutlineEntry("expenditure_categories", "", "expenditure_categories", ModuleConst.BASE_URL + "expenditure-categories"));
            so.addEntry(new OutlineEntry("expenditures", "", "expenditures", ModuleConst.BASE_URL + "expenditures"));
            so.addEntry(new OutlineEntry("activity_types", "", "activity_types", ModuleConst.BASE_URL + "activity-type-categories"));
            so.addEntry(new OutlineEntry("industry_types", "", "industry-types", ModuleConst.BASE_URL + "industry-types"));
            so.addEntry(new OutlineEntry("building_states", "", "building_states", ModuleConst.BASE_URL + "building-states"));
            so.addEntry(new OutlineEntry("building_materials", "", "building_materials", ModuleConst.BASE_URL + "building-materials"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.PASSENGERTRANSPORTTAR.name())) {
            so.addEntry(new OutlineEntry("route_classifications", "", "route_classifications", ModuleConst.BASE_URL + "route-classifications"));
            so.addEntry(new OutlineEntry("route_statuses", "", "route_statuses", ModuleConst.BASE_URL + "route-statuses"));
            so.addEntry(new OutlineEntry("bus_classifications", "", "bus_classifications", ModuleConst.BASE_URL + "bus-classifications"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.ENERGETICSTAR.name())) {
            so.addEntry(new OutlineEntry("eng_infrastruct_obj_classifications", "", "eng_infrastruct_obj_classifications", ModuleConst.BASE_URL + "eng-infrastruct-obj-classifications"));
            so.addEntry(new OutlineEntry("building_states", "", "building_states", ModuleConst.BASE_URL + "building-states"));
            so.addEntry(new OutlineEntry("building_materials", "", "building_materials", ModuleConst.BASE_URL + "building-materials"));
            so.addEntry(new OutlineEntry("activity_types", "", "activity_types", ModuleConst.BASE_URL + "activity-type-categories"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.CONSTRUCTIONTAR.name())) {
            so.addEntry(new OutlineEntry("road_types", "", "road_types", ModuleConst.BASE_URL + "road-types"));
            so.addEntry(new OutlineEntry("road_repair_types", "", "road_repair_types", ModuleConst.BASE_URL + "road-repair-types"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.NATURETAR.name())) {
            so.addEntry(new OutlineEntry("nature_conservation_obj_types", "", "nature_conservation_obj_types", ModuleConst.BASE_URL + "nature-conservation-obj-types"));
            so.addEntry(new OutlineEntry("eng_infrastruct_obj_classifications", "", "eng_infrastruct_obj_classifications", ModuleConst.BASE_URL + "eng-infrastruct-obj-classifications"));
            so.addEntry(new OutlineEntry("real_estate_obj_statuses", "", "real_estate_obj_statuses", ModuleConst.BASE_URL + "real-estate-obj-statuses"));
        } else if (EnvConst.APP_ID.equalsIgnoreCase(AvailableApplicationTypes.INTEGRATIONHUB.name())) {
            so.addEntry(new OutlineEntry("control_types", "", "control_types", ModuleConst.BASE_URL + "control-types"));
            so.addEntry(new OutlineEntry("doc_subjs", "", "document_subjects", ModuleConst.BASE_URL + "document-subjects"));
            so.addEntry(new OutlineEntry("text_template", "", "text_templates", ModuleConst.BASE_URL + "text-templates"));
            so.addEntry(new OutlineEntry("activity_types", "", "activity_types", ModuleConst.BASE_URL + "activity-type-categories"));
            so.addEntry(new OutlineEntry("industry_types", "", "industry-types", ModuleConst.BASE_URL + "industry-types"));
            so.addEntry(new OutlineEntry("nationalities", "", "nationalities", ModuleConst.BASE_URL + "nationalities"));
            so.addEntry(new OutlineEntry("property_codes", "", "property_codes", ModuleConst.BASE_URL + "property-codes"));
            so.addEntry(new OutlineEntry("real_estate_obj_statuses", "", "real_estate_obj_statuses", ModuleConst.BASE_URL + "real-estate-obj-statuses"));
            so.addEntry(new OutlineEntry("land_classifications", "", "land_classifications", ModuleConst.BASE_URL + "land-classifications"));
            so.addEntry(new OutlineEntry("building_states", "", "building_states", ModuleConst.BASE_URL + "building-states"));
            so.addEntry(new OutlineEntry("building_materials", "", "building_materials", ModuleConst.BASE_URL + "building-materials"));
            so.addEntry(new OutlineEntry("memorial_types", "", "memorial_types", ModuleConst.BASE_URL + "memorial-types"));
            so.addEntry(new OutlineEntry("realestate_obj_purposes", "", "realestate_obj_purposes", ModuleConst.BASE_URL + "realestate-obj-purposes"));
            so.addEntry(new OutlineEntry("revenue_categories", "", "revenue_categories", ModuleConst.BASE_URL + "revenue-categories"));
            so.addEntry(new OutlineEntry("revenues", "", "revenues", ModuleConst.BASE_URL + "revenues"));
            so.addEntry(new OutlineEntry("expenditure_categories", "", "expenditure_categories", ModuleConst.BASE_URL + "expenditure-categories"));
            so.addEntry(new OutlineEntry("expenditures", "", "expenditures", ModuleConst.BASE_URL + "expenditures"));
            so.addEntry(new OutlineEntry("eng_infrastruct_obj_classifications", "", "eng_infrastruct_obj_classifications", ModuleConst.BASE_URL + "eng-infrastruct-obj-classifications"));
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
