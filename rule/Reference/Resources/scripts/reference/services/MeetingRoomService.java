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
import reference.dao.MeetingRoomDAO;
import reference.model.MeetingRoom;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("meeting_rooms")
public class MeetingRoomService extends RestProvider {

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
            MeetingRoomDAO dao = new MeetingRoomDAO(session);
            ViewPage<MeetingRoom> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);

            if (user.isSuperUser() || user.getRoles().contains("reference_admin")) {
                _ActionBar actionBar = new _ActionBar(session);
                actionBar.addAction(new _Action("new_", "", "new_"));
                actionBar.addAction(new _Action("del_document", "", _ActionType.DELETE_DOCUMENT));
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("meeting_rooms");
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
            MeetingRoom entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new MeetingRoom();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                MeetingRoomDAO dao = new MeetingRoomDAO(session);
                entity = dao.findByIdentefier(id);
            }

            //
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(new _Action("close", "", _ActionType.CLOSE));
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains("reference_admin")) {
                actionBar.addAction(new _Action("save_close", "", "save_and_close", "", "btn-primary"));
            }

            Outcome outcome = new Outcome();
            outcome.addPayload(entity);
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
    public Response add(MeetingRoom dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, MeetingRoom dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(MeetingRoom dto) {
        _Session session = getSession();
        IUser<Long> user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains("reference_admin")) {
            return null;
        }

        try {
            validate(dto);

            MeetingRoomDAO dao = new MeetingRoomDAO(session);
            MeetingRoom entity;

            if (dto.isNew()) {
                entity = new MeetingRoom();
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

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            MeetingRoomDAO dao = new MeetingRoomDAO(getSession());
            MeetingRoom entity = dao.findByIdentefier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private void validate(MeetingRoom entity) throws _Validation.VException {
        _Validation ve = new _Validation();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        ve.assertValid();
    }
}