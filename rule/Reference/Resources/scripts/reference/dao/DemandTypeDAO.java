package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.DemandType;

public class DemandTypeDAO extends ReferenceDAO<DemandType, UUID> {
	
	public DemandTypeDAO(_Session session) throws DAOException {
		super(DemandType.class, session);
	}
}
