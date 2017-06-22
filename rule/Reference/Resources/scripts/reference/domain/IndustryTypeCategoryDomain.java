package reference.domain;

import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;

import reference.dao.IndustryTypeCategoryDAO;
import reference.model.IndustryTypeCategory;

public class IndustryTypeCategoryDomain extends CommonDomain<IndustryTypeCategory> {

	public IndustryTypeCategoryDomain(_Session ses) throws DAOException {
		super(ses);
		dao = new IndustryTypeCategoryDAO(ses);
	}

	@Override
	public IndustryTypeCategory fillFromDto(IndustryTypeCategory dto, IValidation<IndustryTypeCategory> validation, String fsid)
			throws DTOException, DAOException {
		validation.check(dto);

		IndustryTypeCategory entity;

		if (dto.isNew()) {
			entity = new IndustryTypeCategory();
		} else {
			entity = dao.findById(dto.getId());
		}

		entity.setTitle(dto.getTitle());
		entity.setName(dto.getName());
		entity.setLocName(dto.getLocName());

		return entity;
	}

}
