package reference.services;

import administrator.dao.ModuleDAO;
import administrator.model.Application;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.util.ReflectionUtil;
import reference.dao.TagDAO;
import reference.dao.filter.TagFilter;
import reference.init.ModuleConst;
import reference.model.Tag;
import reference.ui.ViewOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("tags")
public class TagService extends ReferenceService<Tag> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();

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
            outcome.setPayloadTitle("tags");
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
            Set<String> allTagCategories = new HashSet<String>();
            ModuleDAO dao = new ModuleDAO();
            allTagCategories.addAll(Arrays.asList(ModuleConst.TAG_CATEGORIES));
            List<Application> modules = dao.findAll();
            for (Application module : modules) {
                if (module.isOn()) {
                    String[] availableTagCategories = (String[]) ReflectionUtil.getAppConstValue(module.getName(), "TAG_CATEGORIES");
                    allTagCategories.addAll(Arrays.asList(availableTagCategories));
                }
            }
            Outcome outcome = getDefaultRefFormOutcome(id);
            outcome.addPayload("tagCategories", allTagCategories);
            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
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
            entity.setName(extractAnyNameValue(dto));
            entity.setCategory(dto.getCategory());
            entity.setColor(dto.getColor());
            entity.setHidden(dto.isHidden());
            entity.setLocName(dto.getLocName());

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
}
