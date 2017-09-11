package reference.services;

import com.exponentus.common.domain.IDTODomain;
import com.exponentus.common.service.EntityService;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.dataengine.jpa.ISimpleReferenceEntity;
import com.exponentus.env.EnvConst;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.user.IUser;
import reference.ui.Action;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static reference.init.AppConst.ROLE_REFERENCE_ADMIN;

public abstract class ReferenceService<T extends IAppEntity<UUID>, D extends IDTODomain<T>> extends EntityService<T, D> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();

        SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
        ViewPage<T> vp = getDomain().getViewPage(sortParams, params.getPage(), session.getPageSize());

        Outcome outcome = new Outcome();
        IUser user = session.getUser();
        if (user.isSuperUser() || user.getRoles().contains(ROLE_REFERENCE_ADMIN)) {
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(new Action().addNew);
            actionBar.addAction(new Action().deleteDocument);
            outcome.addPayload(actionBar);
        }

        outcome.setTitle(this.getClass().getAnnotation(Path.class).value());
        outcome.addPayload(vp);

        return Response.ok(outcome).build();

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            _Session session = getSession();
            T entity = getDomain().getEntity(id);

            if (entity.getId() == null) {
                ((ISimpleReferenceEntity) entity).setName("");
                entity.setAuthor(session.getUser());
            }

            //
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(new Action().close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains(ROLE_REFERENCE_ADMIN)) {
                actionBar.addAction(new Action().saveAndClose);
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
}
