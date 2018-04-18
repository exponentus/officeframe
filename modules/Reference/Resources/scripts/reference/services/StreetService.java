package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.services.Defended;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import org.apache.commons.collections4.MapUtils;
import reference.dao.StreetDAO;
import reference.dao.filter.StreetFilter;
import reference.model.Street;
import reference.ui.ViewOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("streets")
public class StreetService extends ReferenceService<Street> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            StreetFilter streetFilter = new StreetFilter(params);
            StreetDAO streetDAO = new StreetDAO(session);

            ViewPage<Street> vp = streetDAO.findViewPage(streetFilter, sortParams, params.getPage(), pageSize);
            ViewOptions vo = new ViewOptions();
            vp.setViewPageOptions(vo.getStreetOptions());
            vp.setFilter(vo.getStreetFilter());

            Outcome outcome = new Outcome();
            outcome.setTitle("streets");
            outcome.setPayloadTitle("streets");
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    public Response save(Street dto) {
        _Session session = getSession();

        try {
            validate(dto);

            StreetDAO dao = new StreetDAO(session);
            Street entity;

            if (dto.isNew()) {
                entity = new Street();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(extractAnyNameValue(dto));
            entity.setAltName(dto.getAltName());
            entity.setLocName(dto.getLocName());
            entity.setLocality(dto.getLocality());
            entity.setCityDistrict(dto.getCityDistrict());
            entity.setStreetId(dto.getStreetId());


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


    protected void validate(Street entity) throws DTOException {
        DTOException ve = new DTOException();

        if (MapUtils.isEmpty(entity.getLocName()) || entity.getLocName().values().stream().anyMatch(String::isEmpty)) {
            ve.addError("locName", "required", "field_is_empty");
        }

        if (entity.getLocality() == null) {
            ve.addError("locality", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Defended(false)
    @Path("list/{pageNum}/{pageSize}")
    public Response getViewPage(@PathParam("pageNum") int pageNum, @PathParam("pageSize") int pageSize) {
        _Session session = getSession();
        try {
            Outcome outcome = new Outcome();
            SortParams sortParams = SortParams.desc("regDate");
            StreetDAO streetDAO = new StreetDAO(session);
            ViewPage<Street> vp = streetDAO.findViewPage(sortParams, pageNum, pageSize);
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.setTitle("streets");
            outcome.setPayloadTitle("streets");
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }
}
