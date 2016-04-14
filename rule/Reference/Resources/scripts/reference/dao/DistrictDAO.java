package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;
import reference.model.District;

public class DistrictDAO extends ReferenceDAO<District, UUID> {

	public DistrictDAO(_Session session) {
		super(District.class, session);
	}

}
