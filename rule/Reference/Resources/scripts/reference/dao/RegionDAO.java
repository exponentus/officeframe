package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;
import reference.model.Region;

public class RegionDAO extends ReferenceDAO<Region, UUID> {

	public RegionDAO(_Session session) {
		super(Region.class, session);
	}
}
