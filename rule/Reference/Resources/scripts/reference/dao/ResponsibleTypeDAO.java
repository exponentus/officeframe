package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;

import reference.model.ResponsibleType;

public class ResponsibleTypeDAO extends ReferenceDAO<ResponsibleType, UUID> {

	public ResponsibleTypeDAO(_Session session) {
		super(ResponsibleType.class, session);
	}

}
