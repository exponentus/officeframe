package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.dao.LocalityTypeDAO;
import reference.model.LocalityType;
import reference.model.constants.LocalityCode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("locality-types")
public class LocalityTypeService extends ReferenceService<LocalityType> {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            Outcome outcome = getDefaultRefFormOutcome(id);
            outcome.addPayload("localityCodes", LocalityCode.values());

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }


    public Response save(LocalityType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            LocalityTypeDAO dao = new LocalityTypeDAO(session);
            LocalityType entity;

            if (dto.isNew()) {
                entity = new LocalityType();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setCode(dto.getCode());

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
