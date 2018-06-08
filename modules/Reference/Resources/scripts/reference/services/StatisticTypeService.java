package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.StatisticTypeDAO;
import reference.model.StatisticType;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("statistic-type")
public class StatisticTypeService extends ReferenceService<StatisticType> {

    public Response save(StatisticType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            StatisticTypeDAO dao = new StatisticTypeDAO(session);
            StatisticType entity;

            if (dto.isNew()) {
                entity = new StatisticType();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setCode(dto.getCode());
            entity.setParent(dto.getParent());

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

    protected void validate(StatisticType entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
