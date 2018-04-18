package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.LocalityDAO;
import reference.dao.filter.LocalityFilter;
import reference.model.Locality;
import reference.ui.ViewOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("localities")
public class LocalityService extends ReferenceService<Locality> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            LocalityFilter localityFilter = new LocalityFilter(params);
            LocalityDAO dao = new LocalityDAO(session);

            ViewPage<Locality> vp = dao.findViewPage(localityFilter, sortParams, params.getPage(), pageSize);
            ViewOptions vo = new ViewOptions();
            vp.setViewPageOptions(vo.getLocalityOptions());
            vp.setFilter(vo.getLocalityFilter());

            Outcome outcome = new Outcome();
            outcome.setTitle("localities");
            outcome.setPayloadTitle("localities");
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }


    public Response save(Locality dto) {
        _Session session = getSession();

        try {
            validate(dto);

            LocalityDAO dao = new LocalityDAO(session);
            Locality entity;

            if (dto.isNew()) {
                entity = new Locality();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setType(dto.getType());
            entity.setRegion(dto.getRegion());
            entity.setDistrict(dto.getDistrict());

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


    protected void validate(Locality entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }
        if (entity.getRegion() == null) {
            ve.addError("region", "required", "field_is_empty");
        }
        if (entity.getType() == null) {
            ve.addError("type", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
