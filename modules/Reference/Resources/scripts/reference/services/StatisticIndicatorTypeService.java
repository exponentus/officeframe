package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.StatisticIndicatorTypeDAO;
import reference.model.StatisticIndicatorType;
import reference.model.StatisticType;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("statistic-indicator-type")
public class StatisticIndicatorTypeService extends ReferenceService<StatisticIndicatorType> {

    public Response save(StatisticIndicatorType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            StatisticIndicatorTypeDAO dao = new StatisticIndicatorTypeDAO(session);
            StatisticIndicatorType entity;

            if (dto.isNew()) {
                entity = new StatisticIndicatorType();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setCode(dto.getCode());
            entity.setStatisticType(dto.getStatisticType());
            entity.setUnitType(dto.getUnitType());

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
