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
import reference.dao.AsOfDAO;
import reference.model.AsOf;
import reference.ui.ViewOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("as-of")
@Produces(MediaType.APPLICATION_JSON)
public class AsOfService extends ReferenceService<AsOf> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("asOfByDate"));
            AsOfDAO dao = new AsOfDAO(session);
            ViewPage<AsOf> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            vp.setViewPageOptions(new ViewOptions().getAsOfOptions());

            Outcome outcome = new Outcome();
            outcome.setTitle("as_of");
            outcome.setPayloadTitle("as_of");
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    public Response save(AsOf dto) {
        _Session session = getSession();

        try {
            validate(dto);

            AsOfDAO dao = new AsOfDAO(session);
            AsOf entity;

            if (dto.isNew()) {
                entity = new AsOf();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(extractAnyNameValue(dto));
            entity.setLocName(dto.getLocName());
            entity.setAsOfByDate(dto.getAsOfByDate());
            entity.setAllowedToPublish(dto.isAllowedToPublish());

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

    protected void validate(AsOf entity) throws DTOException {
        DTOException ve = new DTOException();

        if (MapUtils.isEmpty(entity.getLocName()) || entity.getLocName().values().stream().anyMatch(String::isEmpty)) {
            ve.addError("locName", "required:all", "field_is_empty");
        }
        if (entity.getAsOfByDate() == null) {
            ve.addError("asOfByDate", "date", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
