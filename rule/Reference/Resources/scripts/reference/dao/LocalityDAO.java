package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.Locality;

public class LocalityDAO extends ReferenceDAO<Locality, UUID> {
	
	public LocalityDAO(_Session session) throws DAOException {
		super(Locality.class, session);
	}
	
}
