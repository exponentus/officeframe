package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.AsOfDAO;
import reference.model.AsOf;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("as-of")
@Produces(MediaType.APPLICATION_JSON)
public class AsOfService extends ReferenceService<AsOf> {


    public Response save(AsOf dto) {
        _Session session = getSession();

        try {
            validate(dto);

            AsOfDAO dao = new AsOfDAO(session);
            AsOf entity;

            if (dto.isNew()) {
                entity = new AsOf();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(extractAnyNameValue(dto));
            entity.setLocName(dto.getLocName());
            entity.setAsOfByDate(dto.getAsOfByDate());
            entity.setAllowedToPublish(dto.isAllowedToPublish());

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


    protected void validate(AsOf entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getAsOfByDate() == null) {
            ve.addError("asOfByDate", "date", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
