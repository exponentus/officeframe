package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;
import reference.model.Locality;

public class LocalityDAO extends ReferenceDAO<Locality, UUID> {

	public LocalityDAO(_Session session) {
		super(Locality.class, session);
	}

}
