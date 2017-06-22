package reference.domain;

import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;

import reference.dao.InvestmentTypeDAO;
import reference.model.InvestmentType;

public class InvestmentTypeDomain extends CommonDomain<InvestmentType> {

	public InvestmentTypeDomain(_Session ses) throws DAOException {
		super(ses);
		dao = new InvestmentTypeDAO(ses);
	}

	@Override
	public InvestmentType fillFromDto(InvestmentType dto, IValidation<InvestmentType> validation, String fsid)
			throws DTOException, DAOException {
		validation.check(dto);

		InvestmentType entity;

		if (dto.isNew()) {
			entity = new InvestmentType();
		} else {
			entity = dao.findById(dto.getId());
		}

		entity.setTitle(dto.getTitle());
		entity.setName(dto.getName());
		entity.setLocName(dto.getLocName());

		return entity;
	}

}
