package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.dao.ActivityTypeCategoryDAO;
import reference.dao.IndustryTypeDAO;
import reference.dao.filter.IndustryTypeFilter;
import reference.init.DataConst;
import reference.model.ActivityTypeCategory;
import reference.model.IndustryType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("industry-types")
public class IndustryTypeService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser user = session.getUser();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            Outcome outcome = new Outcome();

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
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.setTitle("industry_types");
            outcome.addPayload("contentTitle", "industry_types");
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            _Session session = getSession();
            IndustryType entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new IndustryType();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                IndustryTypeDAO dao = new IndustryTypeDAO(session);
                entity = dao.findByIdentifier(id);
            }


            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "industry_type");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(getDefaultFormActionBar(entity));

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(IndustryType dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, IndustryType dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
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

            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            ActivityTypeCategory cat = new ActivityTypeCategoryDAO(session).findByName(DataConst.ACTIVITY_TYPE_CATEGORY_FOR_INDUSTRY);
            entity.setCategory(cat);

            dao.save(entity);

            Outcome outcome = new Outcome();
            outcome.addPayload(entity);

            return Response.ok(outcome).build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        } catch (DTOException e) {
            return responseValidationError(e);
        }
    }

    private void validate(IndustryType entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
