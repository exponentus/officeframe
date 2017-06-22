package reference.domain;

import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;

import reference.dao.IndustryTypeDAO;
import reference.model.IndustryType;

public class IndustryTypeDomain extends CommonDomain<IndustryType> {

	public IndustryTypeDomain(_Session ses) throws DAOException {
		super(ses);
		dao = new IndustryTypeDAO(ses);
	}

	@Override
	public IndustryType fillFromDto(IndustryType dto, IValidation<IndustryType> validation, String fsid) throws DTOException, DAOException {
		validation.check(dto);

		IndustryType entity;

		if (dto.isNew()) {
			entity = new IndustryType();
		} else {
			entity = dao.findById(dto.getId());
		}

		entity.setTitle(dto.getTitle());
		entity.setName(dto.getName());
		entity.setLocName(dto.getLocName());
		entity.setCategory(dto.getCategory());

		return entity;
	}

}
