package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.RegionType;

public class RegionTypeDAO extends ReferenceDAO<RegionType, UUID> {
	
	public RegionTypeDAO(_Session session) throws DAOException {
		super(RegionType.class, session);
	}
	
}
