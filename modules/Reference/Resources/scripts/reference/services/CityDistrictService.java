package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.CityDistrictDAO;
import reference.dao.filter.CityDistrictFilter;
import reference.model.CityDistrict;
import reference.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

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

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            _Session session = getSession();
            CityDistrict entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new CityDistrict();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                CityDistrictDAO dao = new CityDistrictDAO(session);
                entity = dao.findByIdentifier(id);
            }

            Outcome outcome = new Outcome();
            outcome.setModel(entity);
            outcome.setPayloadTitle("city_district");
            outcome.setFSID(getWebFormData().getFormSesId());
            outcome.addPayload(getDefaultFormActionBar(entity));

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(CityDistrict dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, CityDistrict dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
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
            entity.setName(getEntityName(dto));
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

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            CityDistrictDAO dao = new CityDistrictDAO(getSession());
            CityDistrict entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(CityDistrict entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getLocality() == null) {
            ve.addError("locality", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
