package staff.services;

import com.exponentus.common.service.EntityService;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.actions._ActionBar;
import com.exponentus.user.IUser;
import reference.model.OrgCategory;
import staff.dao.IndividualDAO;
import staff.dao.OrganizationDAO;
import staff.dao.filter.OrganizationFilter;
import staff.domain.IndividualDomain;
import staff.model.Individual;
import staff.model.Organization;
import staff.model.OrganizationLabel;
import staff.ui.Action;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("individuals")
public class IndividualService extends EntityService<Individual, IndividualDomain> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser<Long> user = session.getUser();
        WebFormData params = getWebFormData();

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            OrganizationFilter filter = setUpFilter(new OrganizationFilter(), params);

            OrganizationDAO dao = new OrganizationDAO(session);
            ViewPage<Organization> vp = dao.findAll(filter, sortParams, params.getPage(),
                    params.getNumberValueSilently("limit", session.getPageSize()));

            if (user.isSuperUser() || user.getRoles().contains("staff_admin")) {
                _ActionBar actionBar = new _ActionBar(session);
                actionBar.addAction(new Action().addNew);
                actionBar.addAction(new Action().deleteDocument);
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
            Organization entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new Organization();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                OrganizationDAO dao = new OrganizationDAO(session);
                entity = dao.findByIdentefier(id);
            }

            //
            _ActionBar actionBar = new _ActionBar(session);
            actionBar.addAction(new Action().close);
            if (session.getUser().isSuperUser() || session.getUser().getRoles().contains("staff_admin")) {
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
        _Session session = getSession();
        IUser<Long> user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains("staff_admin")) {
            return null;
        }

        try {
            validate(dto);

            IndividualDAO dao = new IndividualDAO(session);
            Individual entity;

            if (dto.isNew()) {
                entity = new Individual();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
             entity.setBin(dto.getBin());
          //  entity.setLabels(dto.getLabels());

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
            OrganizationDAO dao = new OrganizationDAO(getSession());
            Organization entity = dao.findByIdentefier(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    @Override
    public Response saveForm(Individual dto) {
        return null;
    }

    private void validate(Individual entity) throws DTOException {
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

    public static OrganizationFilter setUpFilter(OrganizationFilter filter, WebFormData params) {
        String orgCategoryId = params.getValueSilently("orgCategory");
        if (!orgCategoryId.isEmpty()) {
            OrgCategory oc = new OrgCategory();
            oc.setId(UUID.fromString(orgCategoryId));
            filter.setOrgCategory(oc);
        }

        String labelId = params.getValueSilently("labels");
        if (!labelId.isEmpty()) {
            OrganizationLabel l = new OrganizationLabel();
            l.setId(UUID.fromString(labelId));
            List<OrganizationLabel> labels = new ArrayList<>();
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
