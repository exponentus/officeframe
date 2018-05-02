package reference.services;

import com.exponentus.common.dao.DAOFactory;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.exception.SecureException;
import com.exponentus.integrationhub.IExternalService;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.log.Lg;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.services.Defended;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import com.exponentus.util.StringUtil;
import org.apache.commons.collections4.MapUtils;
import staff.dao.EmployeeDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.UUID;

public abstract class ReferenceService<T extends SimpleReferenceEntity> extends RestProvider implements IExternalService {
    private static final LanguageCode[] LANG_PREFFERABLE_CODES = {LanguageCode.ENG, LanguageCode.RUS};

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Defended(false)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        Outcome outcome = new Outcome();

        SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        IDAO<T, UUID> dao = DAOFactory.get(session, entityClass);
        ViewPage<T> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
        outcome.addPayload(getDefaultViewActionBar(true));
        String keyword = getClass().getAnnotation(Path.class).value().replace("-", "_");
        outcome.setTitle(keyword);
        outcome.setPayloadTitle(keyword);
        outcome.addPayload(vp);

        return Response.ok(outcome).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            return Response.ok(getDefaultRefFormOutcome(id)).build();
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
                    Class<T> clazz = entityClass;
                    entity = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    Lg.exception(e);
                    return null;
                }
            } else {
                entity = dao.findById(dto.getId());
            }

            entity.setName(extractAnyNameValue(dto));
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

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            IDAO<T, UUID> dao = DAOFactory.get(getSession(), entityClass);

            T entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Defended(false)
    @Path("list/{pageNum}/{pageSize}")
    public Response getViewPage(@PathParam("pageNum") int pageNum, @PathParam("pageSize") int pageSize) {
        return getViewPage();
    }

    @GET
    @Path("id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByIdentifier(@PathParam("id") String identifier) {
        try {
            _Session session = getSession();
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            IDAO<T, UUID> dao = DAOFactory.get(session, entityClass);
            T entity = dao.findById(identifier);
            Map entityMap = entity.extract();
            return Response.ok(entityMap).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    protected void validate(T entity) throws DTOException {
        DTOException ve = new DTOException();

        if (MapUtils.isEmpty(entity.getLocName()) || entity.getLocName().values().stream().anyMatch(String::isEmpty)) {
            ve.addError("locName", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }

    protected static String extractAnyNameValue(SimpleReferenceEntity dto) {
        String name = dto.getName();
        if (name.isEmpty()) {
            String latName = StringUtil.convertStringToURL(dto.getLocName(), LANG_PREFFERABLE_CODES);
            if (latName != null && !latName.isEmpty()) {
                return latName;
            }
            return StringUtil.getRndText();
        } else {
            return name;
        }
    }

    protected Outcome getDefaultRefFormOutcome(String id) throws DAOException {
        _Session session = getSession();
        IUser user = session.getUser();
        T entity;
        String author = "";
        EmployeeDAO employeeDAO = new EmployeeDAO(session);
        boolean isNew = "new".equals(id);
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        if (isNew) {
            try {
                Class<T> clazz = entityClass;
                entity = clazz.newInstance();
                author = employeeDAO.getUserName(user);
            } catch (InstantiationException | IllegalAccessException e) {
                Lg.exception(e);
                return null;
            }
            entity.setName("");
            entity.setAuthor(session.getUser());
        } else {
            IDAO<T, UUID> dao = DAOFactory.get(session, entityClass);
            entity = dao.findById(id);
            IUser authorUser = entity.getAuthor();
            if (authorUser != null) {
                author = employeeDAO.getUserName(authorUser);
            }
        }

        Outcome outcome = new Outcome();
        outcome.setModel(entity);
        outcome.setPayloadTitle(StringUtil.kindToKeyword(entity.getEntityKind()));
        outcome.setFSID(getWebFormData().getFormSesId());
        outcome.addPayload(getDefaultFormActionBar(entity));
        outcome.addPayload("author", author);

        return outcome;
    }
}
