package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.StructureType;

public class StructureTypeDAO extends ReferenceDAO<StructureType, UUID> {
	
	public StructureTypeDAO(_Session session) throws DAOException {
		super(StructureType.class, session);
	}
	
}
