package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.TaskTypeDAO;
import reference.model.TaskType;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("task-types")
public class TaskTypeService extends ReferenceService<TaskType> {

    public Response save(TaskType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            TaskTypeDAO dao = new TaskTypeDAO(session);
            TaskType entity;

            if (dto.isNew()) {
                entity = new TaskType();
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

    protected void validate(TaskType entity) throws DTOException {
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
