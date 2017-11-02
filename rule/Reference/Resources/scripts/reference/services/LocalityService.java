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
import com.exponentus.user.IUser;
import reference.dao.LocalityDAO;
import reference.dao.RegionDAO;
import reference.model.Locality;
import reference.model.Region;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;



@Path("localities")
public class LocalityService extends RestProvider {

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
            String regionId = params.getValueSilently("region");

            LocalityDAO dao = new LocalityDAO(session);
            ViewPage<Locality> vp;

            if (regionId == null || regionId.isEmpty()) {
                vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            } else {
                RegionDAO regionDAO = new RegionDAO(session);
                Region region = regionDAO.findByIdentifier(regionId);
                List<Locality> localities = region.getLocalities();
                vp = new ViewPage<Locality>(localities, localities.size(), 1, 1);
            }

            outcome.addPayload(new ConventionalActionFactory().getViewActionBar(session, true));

            outcome.setTitle("localities");
            outcome.addPayload("contentTitle", "localities");
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
            Locality entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Locality();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                LocalityDAO dao = new LocalityDAO(session);
                entity = dao.findByIdentifier(id);
            }


            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "locality");
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
    public Response add(Locality dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Locality dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
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
            LocalityDAO dao = new LocalityDAO(getSession());
            Locality entity = dao.findByIdentifier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Locality entity) throws DTOException {
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
