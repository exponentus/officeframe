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
import reference.dao.StreetDAO;
import reference.dao.filter.StreetFilter;
import reference.model.Street;
import reference.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

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

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            _Session session = getSession();
            Street entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Street();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                StreetDAO dao = new StreetDAO(session);
                entity = dao.findByIdentifier(id);
            }

            Outcome outcome = new Outcome();
            outcome.setModel(entity);
            outcome.setPayloadTitle("street");
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
    public Response add(Street dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Street dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
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
            entity.setName(dto.getName());
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

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            StreetDAO dao = new StreetDAO(getSession());
            Street entity = dao.findByIdentifier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Street entity) throws DTOException {
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
