package staff.domain;

import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import staff.dao.IndividualDAO;
import staff.model.Individual;

public class IndividualDomain extends CommonDomain<Individual> {

	protected IndividualDomain(_Session ses) throws DAOException {
		super(ses);
		dao = new IndividualDAO(ses);
	}

	@Override
	public Individual fillFromDto(Individual dto, IValidation<Individual> validation, String formSesId) throws DTOException, DAOException {
		return null;
	}

}
