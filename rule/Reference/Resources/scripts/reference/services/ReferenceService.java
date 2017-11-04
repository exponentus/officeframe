package reference.services;

import com.exponentus.common.dao.DAOFactory;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.log.Lg;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.util.StringUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;


public abstract class ReferenceService<T extends SimpleReferenceEntity> extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        Outcome outcome = new Outcome();

        SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        IDAO<T, UUID> dao = DAOFactory.get(session, entityClass);
        ViewPage<T> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
        outcome.addPayload(new ConventionalActionFactory().getViewActionBar(session, true));
        String keyword = getClass().getAnnotation(Path.class).value().replace("-", "_");
        outcome.setTitle(keyword);
        outcome.addPayload("contentTitle", keyword);
        outcome.addPayload(vp);

        return Response.ok(outcome).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            _Session session = getSession();
            T entity;
            boolean isNew = "new".equals(id);
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

            if (isNew) {
                try {
                    Class<T> clazz = (Class<T>) entityClass;
                    entity = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    Lg.exception(e);
                    return null;
                }
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                IDAO<T, UUID> dao = DAOFactory.get(session, entityClass);
                entity = dao.findByIdentifier(id);
            }


            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", StringUtil.kindToKeyword(entity.getEntityKind()));
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(new ConventionalActionFactory().getFormActionBar(session, entity));

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(T dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, T dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(T dto) {
        _Session session = getSession();

        try {
            validate(dto);
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            IDAO<T, UUID> dao = DAOFactory.get(session, entityClass);
            T entity;

            if (dto.isNew()) {
                try {
                    Class<T> clazz = (Class<T>) entityClass;
                    entity = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    Lg.exception(e);
                    return null;
                }
            } else {
                entity = dao.findById(dto.getId());
            }

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

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            IDAO<T, UUID> dao = DAOFactory.get(getSession(), entityClass);

            T entity = dao.findByIdentifier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(T entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
