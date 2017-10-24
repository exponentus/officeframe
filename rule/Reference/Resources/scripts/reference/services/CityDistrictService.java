package reference.services;

import com.exponentus.common.ui.ConventionalActionFactory;
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
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.user.IUser;
import reference.dao.CityDistrictDAO;
import reference.dao.LocalityDAO;
import reference.model.CityDistrict;
import reference.model.Locality;
import reference.ui.Action;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;



@Path("city-districts")
public class CityDistrictService extends RestProvider {

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
            String localityId = params.getValueSilently("locality");

            CityDistrictDAO cityDistrictDAO = new CityDistrictDAO(session);
            ViewPage<CityDistrict> vp;

            if (localityId == null || localityId.isEmpty()) {
                vp = cityDistrictDAO.findViewPage(sortParams, params.getPage(), pageSize);
            } else {
                LocalityDAO localityDAO = new LocalityDAO(session);
                Locality locality = localityDAO.findByIdentefier(localityId);
                List<CityDistrict> streetList = locality.getDistricts();
                vp = new ViewPage<CityDistrict>(streetList, streetList.size(), 1, 1);
            }
            outcome.addPayload(new ConventionalActionFactory().getViewActionBar(session, true));

            outcome.setTitle("city_districts");
            outcome.addPayload("contentTitle", "city_districts");
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
                entity = dao.findByIdentefier(id);
            }


            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "city_district");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(new ConventionalActionFactory().getFormActionBar(session,entity));

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
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setLocality(dto.getLocality());

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
            CityDistrictDAO dao = new CityDistrictDAO(getSession());
            CityDistrict entity = dao.findByIdentefier(id);
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

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }
        if (entity.getLocality() == null) {
            ve.addError("locality", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
