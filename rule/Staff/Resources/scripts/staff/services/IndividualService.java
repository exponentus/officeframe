package staff.services;

import com.exponentus.common.domain.IValidation;
import com.exponentus.common.service.EntityService;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.actions._ActionBar;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.model.OrgCategory;
import staff.dao.IndividualDAO;
import staff.dao.filter.IndividualFilter;
import staff.domain.IndividualDomain;
import staff.model.Individual;
import staff.model.IndividualLabel;
import staff.ui.Action;
import staff.ui.ViewOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static staff.init.AppConst.ROLE_STAFF_ADMIN;

@Path("individuals")
public class IndividualService extends EntityService<Individual, IndividualDomain> {

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
            ViewPage<Individual> vp = dao.findAll(filter, sortParams, params.getPage(),
                    params.getNumberValueSilently("limit", session.getPageSize()));
            vp.setViewPageOptions(new ViewOptions().getIndividualOptions());

            if (user.isSuperUser() || user.getRoles().contains(ROLE_STAFF_ADMIN)) {
                _ActionBar actionBar = new _ActionBar(session);
                Action action = new Action();
                actionBar.addAction(action.addNew);
                actionBar.addAction(action.deleteDocument);
                actionBar.addAction(action.refreshVew);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("individuals");
            outcome.addPayload("contentTitle", "individuals");
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
                entity = dao.findByIdentefier(id);
            }

            //
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(new Action().close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains(ROLE_STAFF_ADMIN)) {
                actionBar.addAction(new Action().saveAndClose);
            }

            Outcome outcome = new Outcome();
            outcome.setTitle("individual");
            outcome.addPayload(entity.getEntityKind(), entity);
            outcome.addPayload("kind", entity.getEntityKind());
            outcome.addPayload("contentTitle", "individual");
            outcome.addPayload(EnvConst.FSID_FIELD_NAME, getWebFormData().getFormSesId());
            outcome.addPayload(actionBar);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @Override
    public Response saveForm(Individual dto) {
        try {
            IndividualDomain domain = new IndividualDomain(getSession());
            Individual entity = domain.fillFromDto(dto, new Validation(), getWebFormData().getFormSesId());
            domain.save(entity);
            return Response.ok(domain.getOutcome(entity)).build();
        } catch (DTOException e) {
            return responseValidationError(e);
        } catch (DAOException | SecureException e) {
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
}
