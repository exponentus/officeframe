package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.DisputeType;

public class DisputeTypeDAO extends ReferenceDAO<DisputeType, UUID> {
	
	public DisputeTypeDAO(_Session session) throws DAOException {
		super(DisputeType.class, session);
	}
	
}
