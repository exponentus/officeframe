package reference.domain;

import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;

import reference.dao.NationalityDAO;
import reference.model.Nationality;

public class NationalityDomain extends CommonDomain<Nationality> {

	public NationalityDomain(_Session ses) throws DAOException {
		super(ses);
		dao = new NationalityDAO(ses);
	}

	@Override
	public Nationality fillFromDto(Nationality dto, IValidation<Nationality> validation, String fsid) throws DTOException, DAOException {
		validation.check(dto);

		Nationality entity = getEntity(dto);

		entity.setTitle(dto.getTitle());
		entity.setName(dto.getName());
		entity.setLocName(dto.getLocName());

		return entity;
	}

}
