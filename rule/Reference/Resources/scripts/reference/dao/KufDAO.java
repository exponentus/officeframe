package reference.dao;

import java.util.UUID;

import kz.lof.scripting._Session;
import reference.model.Kuf;

public class KufDAO extends ReferenceDAO<Kuf, UUID> {

	public KufDAO(_Session session) {
		super(Kuf.class, session);
	}

}
