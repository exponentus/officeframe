package staff.services;

import com.exponentus.common.domain.IValidation;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.actions.ActionBar;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.model.OrgCategory;
import staff.dao.IndividualDAO;
import staff.dao.filter.IndividualFilter;
import staff.model.Individual;
import staff.model.IndividualLabel;
import staff.ui.Action;
import staff.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static staff.init.ModuleConst.ROLE_STAFF_ADMIN;

@Path("individuals")
public class IndividualService extends RestProvider {

    public static IndividualFilter setUpFilter(IndividualFilter filter, WebFormData params) {
        String orgCategoryId = params.getValueSilently("orgCategory");
        if (!orgCategoryId.isEmpty()) {
            OrgCategory oc = new OrgCategory();
            oc.setId(UUID.fromString(orgCategoryId));
        }

        String labelId = params.getValueSilently("labels");
        if (!labelId.isEmpty()) {
            IndividualLabel l = new IndividualLabel();
            l.setId(UUID.fromString(labelId));
            List<IndividualLabel> labels = new ArrayList<>();
            labels.add(l);
            filter.setLabels(labels);
        }

        String keyword = params.getValueSilently("keyword");
        if (!keyword.isEmpty()) {
            filter.setKeyword(keyword);
        }

        return filter;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser user = session.getUser();
        WebFormData params = getWebFormData();

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            IndividualFilter filter = setUpFilter(new IndividualFilter(), params);

            IndividualDAO dao = new IndividualDAO(session);
            ViewPage<Individual> vp = dao.findViewPage(filter, sortParams, params.getPage(),
                    params.getNumberValueSilently("limit", session.getPageSize()));
            vp.setViewPageOptions(new ViewOptions().getIndividualOptions());

            if (user.isSuperUser() || user.getRoles().contains(ROLE_STAFF_ADMIN)) {
                ActionBar actionBar = new ActionBar(session);
                Action action = new Action();
                actionBar.addAction(action.addNew);
                actionBar.addAction(action.deleteDocument);
                actionBar.addAction(action.refreshVew);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("individuals");
            outcome.setPayloadTitle("individuals");
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
            Individual entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Individual();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                IndividualDAO dao = new IndividualDAO(session);
                entity = dao.findByIdentifier(id);
            }

            ActionBar actionBar = new ActionBar(session);
            actionBar.addAction(new Action().close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains(ROLE_STAFF_ADMIN)) {
                actionBar.addAction(new Action().saveAndClose);
            }

            Outcome outcome = new Outcome();
            outcome.setTitle("individual");
            outcome.setModel(entity);
            outcome.setPayloadTitle("individual");
            outcome.setFSID(getWebFormData().getFormSesId());
            outcome.addPayload(actionBar);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Individual dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Individual dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(Individual dto) {
        try {
            new Validation().check(dto);

            IndividualDAO individualDAO = new IndividualDAO(getSession());
            Individual entity;

            if (dto.isNew()) {
                entity = new Individual();
            } else {
                entity = individualDAO.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setBin(dto.getBin());
            entity.setLabels(dto.getLabels());

            individualDAO.save(entity);

            Outcome outcome = new Outcome();
            outcome.setModel(entity);

            return Response.ok(outcome).build();
        } catch (DTOException e) {
            return responseValidationError(e);
        } catch (DAOException | SecureException e) {
            return responseException(e);
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try {
            IndividualDAO dao = new IndividualDAO(getSession());
            Individual entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    private class Validation implements IValidation<Individual> {

        @Override
        public void check(Individual entity) throws DTOException {
            DTOException ve = new DTOException();

            if (entity.getName() == null || entity.getName().isEmpty()) {
                ve.addError("name", "required", "field_is_empty");
            }
            if (entity.getBin() != null && !entity.getBin().isEmpty() && entity.getBin().length() != 12) {
                ve.addError("bin", "len_12", "bin_value_should_be_consist_from_12_symbols");
            }

            if (ve.hasError()) {
                throw ve;
            }
        }
    }
}
