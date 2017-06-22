package reference.domain;

import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;

import reference.dao.InvestedAreaDAO;
import reference.model.InvestedArea;

public class InvestedAreaDomain extends CommonDomain<InvestedArea> {

	public InvestedAreaDomain(_Session ses) throws DAOException {
		super(ses);
		dao = new InvestedAreaDAO(ses);
	}

	@Override
	public InvestedArea fillFromDto(InvestedArea dto, IValidation<InvestedArea> validation, String fsid)
			throws DTOException, DAOException {
		validation.check(dto);

		InvestedArea entity;

		if (dto.isNew()) {
			entity = new InvestedArea();
		} else {
			entity = dao.findById(dto.getId());
		}

		entity.setTitle(dto.getTitle());
		entity.setName(dto.getName());
		entity.setLocName(dto.getLocName());

		return entity;
	}

}
