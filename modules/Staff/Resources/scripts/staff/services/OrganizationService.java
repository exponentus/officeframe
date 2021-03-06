package staff.services;

import com.exponentus.common.dao.DAOFactory;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.common.ui.actions.ActionBar;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.exception.SecureException;
import com.exponentus.log.Lg;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.model.OrgCategory;
import staff.dao.EmployeeDAO;
import staff.dao.OrganizationDAO;
import staff.dao.filter.OrganizationFilter;
import staff.model.Organization;
import staff.model.OrganizationLabel;
import staff.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static staff.init.ModuleConst.ROLE_STAFF_ADMIN;

@Path("organizations")
public class OrganizationService extends StaffService<Organization> {

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        IUser user = session.getUser();
        WebFormData params = getWebFormData();

        try {
            Outcome outcome = new Outcome();

            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            OrganizationFilter filter = setUpFilter(new OrganizationFilter(), params);

            OrganizationDAO dao = new OrganizationDAO(session);
            ViewPage<Organization> vp = dao.findViewPage(filter, sortParams, params.getPage(),
                    params.getNumberValueSilently("limit", session.getPageSize()));
            ViewOptions viewOptions = new ViewOptions();
            vp.setViewPageOptions(viewOptions.getOrgOptions());
            vp.setFilter(viewOptions.getOrgFilter());

            if (user.isSuperUser() || user.getRoles().contains(ROLE_STAFF_ADMIN)) {
                ActionBar actionBar = getDefaultViewActionBar(true);
                outcome.addPayload(actionBar);
            }

            outcome.setTitle("organizations");
            outcome.setPayloadTitle("organizations");
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
            String author = "";
            EmployeeDAO employeeDAO = new EmployeeDAO(session);
            if (isNew) {
                entity = new Organization();
                author = employeeDAO.getUserName(session.getUser());
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                OrganizationDAO dao = new OrganizationDAO(session);
                entity = dao.findById(id);
                IUser authorUser = entity.getAuthor();
                if (authorUser != null) {
                    author = employeeDAO.getUserName(authorUser);
                }
            }
            Outcome outcome = new Outcome();
            outcome.setTitle("organization");
            outcome.setModel(entity);
            outcome.setPayloadTitle("organization");
            outcome.setFSID(getWebFormData().getFormSesId());
            outcome.addPayload(getDefaultFormActionBar(entity));
            outcome.addPayload("author", author);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Organization dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Organization dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(Organization dto) {
        _Session session = getSession();
        IUser user = session.getUser();

        if (!user.isSuperUser() && !user.getRoles().contains(ROLE_STAFF_ADMIN)) {
            return null;
        }

        try {
            validate(dto);

            OrganizationDAO dao = new OrganizationDAO(session);
            Organization entity;

            if (dto.isNew()) {
                entity = new Organization();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setRank(dto.getRank());
            entity.setOrgCategory(dto.getOrgCategory());
            entity.setBizID(dto.getBizID());
            entity.setLabels(dto.getLabels());

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
            OrganizationDAO dao = new OrganizationDAO(getSession());
            Organization entity = dao.findById(id);
            if (entity != null) {
                dao.delete(entity);
            }
            return Response.noContent().build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        }
    }

    protected void validate(Organization entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }
        if (entity.getOrgCategory() == null) {
            ve.addError("orgCategory", "required", "field_is_empty");
        }
        if (entity.getBizID() != null && !entity.getBizID().isEmpty() && entity.getBizID().trim().length() != 12) {
            ve.addError("bin", "len:12", "bin_value_should_be_consist_from_12_symbols");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
