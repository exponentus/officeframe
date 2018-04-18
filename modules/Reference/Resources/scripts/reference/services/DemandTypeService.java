package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.DemandTypeDAO;
import reference.model.DemandType;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("demand-types")
public class DemandTypeService extends ReferenceService<DemandType> {

    public Response save(DemandType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            DemandTypeDAO dao = new DemandTypeDAO(session);
            DemandType entity;

            if (dto.isNew()) {
                entity = new DemandType();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setPrefix(dto.getPrefix());

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


    protected void validate(DemandType entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (entity.getPrefix() == null || entity.getPrefix().isEmpty()) {
            ve.addError("prefix", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
