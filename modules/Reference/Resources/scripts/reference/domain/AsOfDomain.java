package reference.domain;

import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.AsOfDAO;
import reference.model.AsOf;

import java.util.HashMap;
import java.util.Map;

public class AsOfDomain extends CommonDomain<AsOf> {

    public AsOfDomain(_Session ses) throws DAOException {
        super(ses);
        dao = new AsOfDAO(ses);
    }

    @Override
    public AsOf fillFromDto(AsOf dto, IValidation<AsOf> validation, String fsid)
            throws DTOException, DAOException {
        validation.check(dto);

        AsOf entity = getEntity(dto);
        //  entity.setSchema(dto.getSchema());
        entity.setLocName(dto.getLocName());

        if (entity.getAsOfByDate() == null) {
            entity.setAsOfByDate(dto.getAsOfByDate());
        }

        if (entity.isNew()) {
            entity.setAuthor(ses.getUser());
            entity.setName(dto.getName());
            entity.setTitle(entity.getName());
        }

        return entity;
    }

    @Override
    public Outcome getOutcome(AsOf entity) {
        Outcome result = super.getOutcome(entity);
        Map<String, Boolean> disabledFields = new HashMap<>();
        if (!entity.isNew()) {
            disabledFields.put("name", true);
            if (entity.getAsOfByDate() != null) {
                disabledFields.put("asOfByDate", true);
            }
        }
        result.addPayload("disabledFields", disabledFields);
        return result;
    }
}
