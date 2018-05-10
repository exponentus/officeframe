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
import reference.dao.CountryDAO;
import reference.dao.RegionDAO;
import reference.model.Country;
import reference.model.Region;
import reference.ui.ViewOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("regions")
public class RegionService extends ReferenceService<Region> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            String countryId = params.getValueSilently("country");

            RegionDAO dao = new RegionDAO(session);
            ViewPage<Region> vp;

            if (countryId != null && !countryId.isEmpty()) {
                CountryDAO countryDAO = new CountryDAO(session);
                Country country = countryDAO.findById(countryId);
                List<Region> regionList = country.getRegions();
                vp = new ViewPage<Region>(regionList, regionList.size(), 1, 1);
            } else {
                vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            }
            vp.setViewPageOptions(new ViewOptions().getRegionOptions());

            Outcome outcome = new Outcome();
            outcome.setTitle("regions");
            outcome.setPayloadTitle("regions");
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    public Response save(Region dto) {
        _Session session = getSession();

        try {
            validate(dto);

            RegionDAO dao = new RegionDAO(session);
            Region entity;

            if (dto.isNew()) {
                entity = new Region();
            } else {
                entity = dao.findById(dto.getId());
            }

            entity.setName(extractAnyNameValue(dto));
            entity.setLocName(dto.getLocName());
            entity.setType(dto.getType());
            entity.setCountry(dto.getCountry());
            entity.setPrimary(dto.isPrimary());
            entity.setCoordinates(dto.getCoordinates());

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

    protected void validate(Region entity) throws DTOException {
        DTOException ve = new DTOException();

        if (MapUtils.isEmpty(entity.getLocName()) || entity.getLocName().values().stream().anyMatch(String::isEmpty)) {
            ve.addError("locName", "required:all", "field_is_empty");
        }
        if (entity.getType() == null) {
            ve.addError("type", "required", "field_is_empty");
        }
        if (entity.getCountry() == null) {
            ve.addError("country", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
