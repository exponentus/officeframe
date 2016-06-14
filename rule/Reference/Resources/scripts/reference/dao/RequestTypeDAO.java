package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;

import reference.model.RequestType;

public class RequestTypeDAO extends ReferenceDAO<RequestType, UUID> {

	public RequestTypeDAO(_Session session) {
		super(RequestType.class, session);
	}

}