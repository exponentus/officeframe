package reference.services;

import reference.dao.NatureConservationObjTypeDAO;
import reference.model.EngInfrastructObjClassification;
import reference.model.NatureConservationObjType;
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
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;


@Path("nature-conservation-obj-types")
public class NatureConservationObjTypeService extends ReferenceService<NatureConservationObjType> {

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
            NatureConservationObjTypeDAO dao = new NatureConservationObjTypeDAO(session);

            ViewPage<NatureConservationObjType> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.setTitle("nature_conservation_obj_type");
            outcome.addPayload("contentTitle", "nature_conservation_obj_type");
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
            NatureConservationObjType entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new NatureConservationObjType();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                NatureConservationObjTypeDAO dao = new NatureConservationObjTypeDAO(session);
                entity = dao.findByIdentifier(id);
            }


            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "nature_conservation_obj_type");
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
    public Response add(NatureConservationObjType dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, NatureConservationObjType dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(NatureConservationObjType dto) {
        _Session session = getSession();


        try {
            validate(dto);

            NatureConservationObjTypeDAO dao = new NatureConservationObjTypeDAO(session);
            NatureConservationObjType entity;

            if (dto.isNew()) {
                entity = new NatureConservationObjType();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());

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
            NatureConservationObjTypeDAO dao = new NatureConservationObjTypeDAO(getSession());
            NatureConservationObjType entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(NatureConservationObjType entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}