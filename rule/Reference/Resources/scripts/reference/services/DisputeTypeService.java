package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.ServiceDescriptor;
import com.exponentus.rest.ServiceMethod;
import com.exponentus.rest.outgoingpojo.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.user.IUser;
import com.exponentus.user.SuperUser;
import reference.dao.DisputeTypeDAO;
import reference.model.DisputeType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("dispute_types")
public class DisputeTypeService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser<Long> user = session.getUser();
        WebFormData params = getWebFormData();
        int pageSize = session.pageSize;

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            DisputeTypeDAO dao = new DisputeTypeDAO(session);
            ViewPage<DisputeType> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

            if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
                _ActionBar actionBar = new _ActionBar(session);
                actionBar.addAction(new _Action("new_", "", "new_"));
                actionBar.addAction(new _Action("del_document", "", _ActionType.DELETE_DOCUMENT));
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("dispute_types");
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
            DisputeType entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new DisputeType();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                DisputeTypeDAO dao = new DisputeTypeDAO(session);
                entity = dao.findById(id);
            }

            //
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(new _Action("close", "", _ActionType.CLOSE));
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains("reference_admin")) {
                actionBar.addAction(new _Action("save_close", "", _ActionType.SAVE_AND_CLOSE));
            }

            Outcome outcome = new Outcome();
            outcome.addPayload(entity);
            outcome.addPayload(actionBar);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(DisputeType dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, DisputeType dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(DisputeType dto) {
        _Session session = getSession();
        IUser<Long> user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains("reference_admin")) {
            return null;
        }

        try {
            validate(dto);

            DisputeTypeDAO dao = new DisputeTypeDAO(session);
            DisputeType entity;

            if (dto.isNew()) {
                entity = new DisputeType();
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
        } catch (_Validation.VException e) {
            return responseValidationError(e.getValidation());
        }
    }

    private void validate(DisputeType entity) throws _Validation.VException {
        _Validation ve = new _Validation();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        ve.assertValid();
    }

    @Override
    public ServiceDescriptor updateDescription(ServiceDescriptor sd) {
        sd.setName(getClass().getName());
        ServiceMethod m = new ServiceMethod();
        m.setMethod(HttpMethod.GET);
        m.setURL("/" + sd.getAppName() + sd.getUrlMapping());
        sd.addMethod(m);
        return sd;
    }
}
