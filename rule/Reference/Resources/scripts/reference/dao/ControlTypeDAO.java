package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;

import reference.model.ControlType;

public class ControlTypeDAO extends ReferenceDAO<ControlType, UUID> {

	public ControlTypeDAO(_Session session) {
		super(ControlType.class, session);
	}

}
