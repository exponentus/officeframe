package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.dao.ControlTypeDAO;
import reference.model.ControlType;
import reference.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("control-types")
public class ControlTypeService extends ReferenceService<ControlType> {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            Outcome outcome = getDefaultRefFormOutcome(id);
            if (((IAppEntity<UUID>)outcome.getModel()).isNew()){
                ((ControlType)outcome.getModel()).setDefaultHours(30);
            }
            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(ControlType dto) {
        dto.setId(null);
        return save(dto);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, ControlType dto) {
        dto.setId(UUID.fromString(id));
        return save(dto);
    }

    public Response save(ControlType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            ControlTypeDAO dao = new ControlTypeDAO(session);
            ControlType entity;

            if (dto.isNew()) {
                entity = new ControlType();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setDefaultHours(dto.getDefaultHours());

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
