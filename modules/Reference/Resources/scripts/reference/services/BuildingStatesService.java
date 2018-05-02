package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.BuildingStateDAO;
import reference.model.BuildingState;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("building-states")
public class BuildingStatesService extends ReferenceService<BuildingState> {

    public Response save(BuildingState dto) {
        _Session session = getSession();

        try {
            validate(dto);

            BuildingStateDAO dao = new BuildingStateDAO(session);
            BuildingState entity;

            if (dto.isNew()) {
                entity = new BuildingState();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setRequireDate(dto.isRequireDate());

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
