package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.Region;

public class RegionDAO extends ReferenceDAO<Region, UUID> {
	
	public RegionDAO(_Session session) throws DAOException {
		super(Region.class, session);
	}
}
