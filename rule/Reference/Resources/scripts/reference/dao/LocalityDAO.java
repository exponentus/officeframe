package reference.dao;

import java.util.UUID;

import kz.lof.scripting._Session;
import reference.model.Locality;

public class LocalityDAO extends ReferenceDAO<Locality, UUID> {

	public LocalityDAO(_Session session) {
		super(Locality.class, session);
	}

}
