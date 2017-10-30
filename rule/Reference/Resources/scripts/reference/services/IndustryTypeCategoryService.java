package reference.services;

import com.exponentus.common.ui.ConventionalActionFactory;
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
import reference.dao.IndustryTypeCategoryDAO;
import reference.model.IndustryTypeCategory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;


@Path("industry-type-categories")
public class IndustryTypeCategoryService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser user = session.getUser();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            IndustryTypeCategoryDAO dao = new IndustryTypeCategoryDAO(session);
            ViewPage<IndustryTypeCategory> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            outcome.addPayload(new ConventionalActionFactory().getViewActionBar(session, true));
            outcome.setTitle("industry_type_categories");
            outcome.addPayload("contentTitle", "industry_type_categories");
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
            IndustryTypeCategory entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new IndustryTypeCategory();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                IndustryTypeCategoryDAO dao = new IndustryTypeCategoryDAO(session);
                entity = dao.findByIdentefier(id);
            }



            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "industry_type_category");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(new ConventionalActionFactory().getFormActionBar(session,entity));

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(IndustryTypeCategory dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, IndustryTypeCategory dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(IndustryTypeCategory dto) {
        _Session session = getSession();


        try {
            validate(dto);

            IndustryTypeCategoryDAO dao = new IndustryTypeCategoryDAO(session);
            IndustryTypeCategory entity;

            if (dto.isNew()) {
                entity = new IndustryTypeCategory();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setTitle(dto.getTitle());
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());

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

    private void validate(IndustryTypeCategory entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
