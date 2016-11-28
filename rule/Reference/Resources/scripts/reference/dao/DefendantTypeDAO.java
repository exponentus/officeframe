package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.DefendantType;

public class DefendantTypeDAO extends ReferenceDAO<DefendantType, UUID> {
	
	public DefendantTypeDAO(_Session session) throws DAOException {
		super(DefendantType.class, session);
	}
	
}
