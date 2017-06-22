package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.Nationality;

public class NationalityDAO extends ReferenceDAO<Nationality, UUID> {

	public NationalityDAO(_Session session) throws DAOException {
		super(Nationality.class, session);
	}

}
