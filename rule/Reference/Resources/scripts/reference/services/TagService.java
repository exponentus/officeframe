package reference.services;

import administrator.dao.ModuleDAO;
import administrator.model.Application;
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
import com.exponentus.util.ReflectionUtil;
import reference.dao.TagDAO;
import reference.dao.filter.TagFilter;
import reference.init.ModuleConst;
import reference.model.Tag;
import reference.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("tags")
public class TagService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser user = session.getUser();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            TagDAO dao = new TagDAO(session);

            boolean withHidden = params.getBoolSilently("hidden");
            String category = params.getValueSilently("category");
            TagFilter filter = new TagFilter();
            filter.setCategory(category);
            filter.setWithHidden(withHidden);

            ViewPage<Tag> vp = dao.findViewPage(filter, sortParams, 0, 0);
            ViewOptions vo = new ViewOptions();
            vp.setViewPageOptions(vo.getTagOptions());
            vp.setFilter(vo.getTagFilter());

            Outcome outcome = new Outcome();
            outcome.setTitle("tags");
            outcome.addPayload("contentTitle", "tags");
            outcome.addPayload(getDefaultViewActionBar(true));
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
            Tag entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Tag();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                TagDAO dao = new TagDAO(session);
                entity = dao.findByIdentifier(id);
            }

            Set<String> allTagCategories = new HashSet<String>();
            ModuleDAO dao = new ModuleDAO();
            allTagCategories.addAll(Arrays.asList(ModuleConst.TAG_CATEGORIES));
            List<Application> modules = dao.findAll().getResult();
            for (Application module : modules) {
                if (module.isOn()) {
                    String[] availableTagCategories = (String[]) ReflectionUtil.getAppConstValue(module.getName(), "TAG_CATEGORIES");
                    allTagCategories.addAll(Arrays.asList(availableTagCategories));
                }
            }

            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "tag");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(getDefaultFormActionBar(entity));
            outcome.addPayload("tagCategories", allTagCategories);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Tag dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Tag dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(Tag dto) {
        _Session session = getSession();

        try {
            validate(dto);

            TagDAO dao = new TagDAO(session);
            Tag entity;

            if (dto.isNew()) {
                entity = new Tag();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setCategory(dto.getCategory());
            entity.setColor(dto.getColor());
            entity.setHidden(dto.isHidden());
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

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            TagDAO dao = new TagDAO(getSession());
            Tag entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Tag entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
