package staff.services;

import com.exponentus.common.dao.DAOFactory;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.integrationhub.IExternalService;
import com.exponentus.integrationhub.jpa.IIntegratableEntity;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.services.Defended;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.UUID;

public abstract class StaffService<T extends SimpleReferenceEntity> extends RestProvider implements IExternalService {

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
        outcome.addPayload(getDefaultViewActionBar());
        String keyword = getClass().getAnnotation(Path.class).value().replace("-", "_");
        outcome.setTitle(keyword);
        outcome.setPayloadTitle(keyword);
        outcome.addPayload(vp);

        return Response.ok(outcome).build();
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
            Map entityMap = entity.extract(session);
            return Response.ok(entityMap).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }
}
