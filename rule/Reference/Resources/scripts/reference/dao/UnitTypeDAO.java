package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.BuildingMaterial;

public class UnitTypeDAO extends ReferenceDAO<BuildingMaterial, UUID> {
	
	public UnitTypeDAO(_Session session) throws DAOException {
		super(BuildingMaterial.class, session);
	}
	
}
