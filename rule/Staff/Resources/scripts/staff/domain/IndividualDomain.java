package staff.domain;

import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import staff.dao.IndividualDAO;
import staff.model.Individual;

public class IndividualDomain extends CommonDomain<Individual> {

    public IndividualDomain(_Session ses) throws DAOException {
        super(ses);
        dao = new IndividualDAO(ses);
    }

    @Override
    public Individual fillFromDto(Individual dto, IValidation<Individual> validation, String formSesId) throws DTOException, DAOException {
        validation.check(dto);

        Individual entity;

        if (dto.isNew()) {
            entity = new Individual();
        } else {
            entity = dao.findById(dto.getId());
        }

        entity.setName(dto.getName());
        entity.setLocName(dto.getLocName());
        entity.setBin(dto.getBin());
        entity.setLabels(dto.getLabels());
        return entity;
    }
}
