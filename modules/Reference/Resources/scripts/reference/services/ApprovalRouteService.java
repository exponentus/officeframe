package reference.services;

import administrator.dao.LanguageDAO;
import com.exponentus.common.model.constants.ApprovalSchemaType;
import com.exponentus.common.model.constants.ApprovalType;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.ApprovalRouteDAO;
import reference.dto.converter.ApprovalRouteDtoConverter;
import reference.model.ApprovalRoute;
import reference.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("approval-routes")
public class ApprovalRouteService extends ReferenceService<ApprovalRoute> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            SortParams sortParams = params.getSortParams(SortParams.desc("regDate"));
            ApprovalRouteDAO dao = new ApprovalRouteDAO(session);
            ViewPage<ApprovalRoute> vp = dao.findViewPage(sortParams, params.getPage(), pageSize);
            vp.setViewPageOptions(new ViewOptions().getApprovalRouteOptions());
            if (!"$all".equals(params.getValueSilently("fields"))) {
                vp.setResult(new ApprovalRouteDtoConverter().convert(vp.getResult()));
            }

            Outcome outcome = new Outcome();
            outcome.setTitle("approval_routes");
            outcome.setPayloadTitle("approval_route");
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
            Outcome outcome = getDefaultRefFormOutcome(id);
            // include
            outcome.addPayload("languages", new LanguageDAO(session).findAllActivated());
            outcome.addPayload("approvalSchemaTypes", ApprovalSchemaType.values());
            outcome.addPayload("approvalTypes", ApprovalType.values());

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(ApprovalRoute dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, ApprovalRoute dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(ApprovalRoute dto) {
        _Session session = getSession();

        try {
            validate(dto);

            ApprovalRouteDAO dao = new ApprovalRouteDAO(session);
            ApprovalRoute entity;

            if (dto.isNew()) {
                entity = new ApprovalRoute();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setOn(dto.isOn());
            entity.setVersionsSupport(dto.isVersionsSupport());
            entity.setCategory(dto.getCategory());
            entity.setSchema(dto.getSchema());
            entity.setRouteBlocks(dto.getRouteBlocks());
            entity.setLocName(dto.getLocName());
            entity.setLocalizedDescr(dto.getLocalizedDescr());

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


}
