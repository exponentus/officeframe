package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.DistrictDAO;
import reference.dao.RegionDAO;
import reference.model.District;
import reference.model.Region;
import reference.ui.ViewOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("districts")
public class DistrictService extends ReferenceService<District> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            String regionId = params.getValueSilently("region");

            DistrictDAO dao = new DistrictDAO(session);
            ViewPage<District> vp;

            if (regionId == null || regionId.isEmpty()) {
                vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            } else {
                RegionDAO regionDAO = new RegionDAO(session);
                Region region = regionDAO.findById(regionId);
                List<District> districts = region.getDistricts();
                vp = new ViewPage<District>(districts, districts.size(), 1, 1);
            }
            outcome.addPayload(getDefaultViewActionBar(true));
            vp.setViewPageOptions(new ViewOptions().getDistrictOptions());

            outcome.setTitle("districts");
            outcome.setPayloadTitle("districts");
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
            Outcome outcome = getDefaultRefFormOutcome(id);
            outcome.addPayload("mapsApiKey", Environment.mapsApiKey);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    public Response save(District dto) {
        _Session session = getSession();

        try {
            validate(dto);

            DistrictDAO dao = new DistrictDAO(session);
            District entity;

            if (dto.isNew()) {
                entity = new District();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(extractAnyNameValue(dto));
            entity.setLocName(dto.getLocName());
            entity.setRegion(dto.getRegion());

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
}
