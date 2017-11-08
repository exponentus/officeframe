package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.dao.CountryDAO;
import reference.dao.RegionDAO;
import reference.model.Country;
import reference.model.Region;
import reference.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;


@Path("regions")
public class RegionService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser user = session.getUser();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            String countryId = params.getValueSilently("country");

            RegionDAO dao = new RegionDAO(session);
            ViewPage<Region> vp;

            if (countryId != null && !countryId.isEmpty()) {
                CountryDAO countryDAO = new CountryDAO(session);
                Country country = countryDAO.findByIdentifier(countryId);
                List<Region> regionList = country.getRegions();
                vp = new ViewPage<Region>(regionList, regionList.size(), 1, 1);
            } else {
                vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            }
            outcome.addPayload(getDefaultViewActionBar(true));
            vp.setViewPageOptions(new ViewOptions().getRegionOptions());

            outcome.setTitle("regions");
            outcome.addPayload("contentTitle", "regions");
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
            Region entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Region();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                RegionDAO dao = new RegionDAO(session);
                entity = dao.findByIdentifier(id);
            }


            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "region");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(getDefaultFormActionBar(entity));

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Region dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Region dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
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

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setType(dto.getType());
            entity.setCountry(dto.getCountry());
            entity.setPrimary(dto.isPrimary());
            entity.setOrgCoordinates(dto.getOrgCoordinates());

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
            RegionDAO dao = new RegionDAO(getSession());
            Region entity = dao.findByIdentifier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Region entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
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
