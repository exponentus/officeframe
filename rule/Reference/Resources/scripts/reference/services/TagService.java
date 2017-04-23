package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting._Validation;
import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.scripting.actions._ActionType;
import com.exponentus.user.IUser;
import reference.dao.TagDAO;
import reference.model.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("tags")
public class TagService extends RestProvider {

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
            TagDAO dao = new TagDAO(session);
            ViewPage<Tag> vp;

            boolean withHidden = params.getBoolSilently("hidden");
            String category = params.getValueSilently("category");

            if (category.isEmpty() || (!"software_developing_task".equals(category))
                    && !"software_developing_demand".equals(category)) {
                vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            } else {
                vp = dao.findAllByCategoryAndVisibility(category, withHidden, 1, 0);
            }

            if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
                _ActionBar actionBar = new _ActionBar(session);
                actionBar.addAction(Action.addNew);
                actionBar.addAction(Action.deleteDocument);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("tags");
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
            Tag entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Tag();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                TagDAO dao = new TagDAO(session);
                entity = dao.findByIdentefier(id);
            }

            //
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(Action.close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains("reference_admin")) {
                actionBar.addAction(Action.saveAndClose);
            }

            Outcome outcome = new Outcome();
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(actionBar);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Tag dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Tag dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(Tag dto) {
        _Session session = getSession();
        IUser<Long> user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains("reference_admin")) {
            return null;
        }

        try {
            validate(dto);

            TagDAO dao = new TagDAO(session);
            Tag entity;

            if (dto.isNew()) {
                entity = new Tag();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setCategory(dto.getCategory());
            entity.setColor(dto.getColor());
            entity.setHidden(dto.isHidden());
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

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            TagDAO dao = new TagDAO(getSession());
            Tag entity = dao.findByIdentefier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(Tag entity) throws _Validation.VException {
        _Validation ve = new _Validation();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        ve.assertValid();
    }
}
