package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.RevenueDAO;
import reference.model.Revenue;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("revenues")
public class RevenueService extends ReferenceService<Revenue> {

    public Response save(Revenue dto) {
        _Session session = getSession();

        try {
            validate(dto);

            RevenueDAO dao = new RevenueDAO(session);
            Revenue entity;

            if (dto.isNew()) {
                entity = new Revenue();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setCategory(dto.getCategory());
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());

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

    private void validate(Revenue entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getCategory() == null) {
            ve.addError("category", "required", "field_is_empty");
        }
        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
