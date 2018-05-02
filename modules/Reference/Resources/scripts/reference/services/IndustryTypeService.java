package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import org.apache.commons.collections4.MapUtils;
import reference.dao.ActivityTypeCategoryDAO;
import reference.dao.IndustryTypeDAO;
import reference.dao.filter.IndustryTypeFilter;
import reference.init.ModuleConst;
import reference.model.ActivityTypeCategory;
import reference.model.IndustryType;
import reference.ui.ViewOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("industry-types")
public class IndustryTypeService extends ReferenceService<IndustryType> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            IndustryTypeFilter filter = new IndustryTypeFilter();
            ActivityTypeCategory activityTypeCategory;
            String categoryId = params.getStringValueSilently("category", "");
            if (!categoryId.isEmpty()) {
                activityTypeCategory = new ActivityTypeCategory();
                activityTypeCategory.setId(UUID.fromString(categoryId));
                filter.setActivityTypeCategory(activityTypeCategory);
            }
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            IndustryTypeDAO dao = new IndustryTypeDAO(session);
            ViewPage<IndustryType> vp = dao.findViewPage(filter, sortParams, params.getPage(), pageSize);
            vp.setViewPageOptions(new ViewOptions().getIndustryTypeOptions());

            Outcome outcome = new Outcome();
            outcome.setTitle("industry_types");
            outcome.setPayloadTitle("industry_types");
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    public Response save(IndustryType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            IndustryTypeDAO dao = new IndustryTypeDAO(session);
            IndustryType entity;

            if (dto.isNew()) {
                entity = new IndustryType();
            } else {
                entity = dao.findById(dto.getId());
            }

            entity.setName(extractAnyNameValue(dto));
            entity.setLocName(dto.getLocName());
            ActivityTypeCategory cat = new ActivityTypeCategoryDAO(session).findByName(ModuleConst.ACTIVITY_TYPE_CATEGORY_FOR_INDUSTRY);
            entity.setCategory(cat);

            dao.save(entity);

            Outcome outcome = new Outcome();
            outcome.setModel(entity);

            return Response.ok(outcome).build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        } catch (DTOException e) {
            return responseValidationError(e);
        }
    }

    protected void validate(IndustryType entity) throws DTOException {
        DTOException ve = new DTOException();

        if (MapUtils.isEmpty(entity.getLocName()) || entity.getLocName().values().stream().anyMatch(String::isEmpty)) {
            ve.addError("locName", "required", "field_is_empty");
        }
        if (entity.getCategory() == null) {
            ve.addError("category", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
