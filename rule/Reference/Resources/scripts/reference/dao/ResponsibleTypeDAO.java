package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.ResponsibleType;

public class ResponsibleTypeDAO extends ReferenceDAO<ResponsibleType, UUID> {
	
	public ResponsibleTypeDAO(_Session session) throws DAOException {
		super(ResponsibleType.class, session);
	}
	
}
