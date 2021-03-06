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
import reference.dao.CityDistrictDAO;
import reference.dao.filter.CityDistrictFilter;
import reference.model.CityDistrict;
import reference.ui.ViewOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("city-districts")
public class CityDistrictService extends ReferenceService<CityDistrict> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            CityDistrictFilter cityDistrictFilter = new CityDistrictFilter(params);
            CityDistrictDAO cityDistrictDAO = new CityDistrictDAO(session);

            ViewPage<CityDistrict> vp = cityDistrictDAO.findViewPage(cityDistrictFilter, sortParams, params.getPage(), pageSize);
            ViewOptions vo = new ViewOptions();
            vp.setViewPageOptions(vo.getCityDistrictOptions());
            // vp.setFilter(vo.getCityDistrictFilter());

            Outcome outcome = new Outcome();
            outcome.setTitle("city_districts");
            outcome.setPayloadTitle("city_districts");
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    public Response save(CityDistrict dto) {
        _Session session = getSession();

        try {
            validate(dto);

            CityDistrictDAO dao = new CityDistrictDAO(session);
            CityDistrict entity;

            if (dto.isNew()) {
                entity = new CityDistrict();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(extractAnyNameValue(dto));
            entity.setLocName(dto.getLocName());
            entity.setLocality(dto.getLocality());

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

    protected void validate(CityDistrict entity) throws DTOException {
        DTOException ve = new DTOException();

        if (MapUtils.isEmpty(entity.getLocName()) || entity.getLocName().values().stream().anyMatch(String::isEmpty)) {
            ve.addError("locName", "required:all", "field_is_empty");
        }
        if (entity.getLocality() == null) {
            ve.addError("locality", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
