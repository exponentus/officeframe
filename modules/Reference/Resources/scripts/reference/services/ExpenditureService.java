package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.ExpenditureDAO;
import reference.model.Expenditure;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("expenditures")
public class ExpenditureService extends ReferenceService<Expenditure> {

    public Response save(Expenditure dto) {
        _Session session = getSession();

        try {
            validate(dto);

            ExpenditureDAO dao = new ExpenditureDAO(session);
            Expenditure entity;

            if (dto.isNew()) {
                entity = new Expenditure();
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

    protected void validate(Expenditure entity) throws DTOException {
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
