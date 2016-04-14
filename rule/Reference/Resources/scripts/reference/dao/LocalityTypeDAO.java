package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;
import reference.model.LocalityType;

public class LocalityTypeDAO extends ReferenceDAO<LocalityType, UUID> {

	public LocalityTypeDAO(_Session session) {
		super(LocalityType.class, session);
	}

}
