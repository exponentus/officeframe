package reference.dao;

import com.exponentus.scripting._Session;
import reference.model.RequestType;
import reference.model.ResponsibleType;

import java.util.UUID;

public class RequestTypeDAO extends ReferenceDAO<RequestType, UUID> {

	public RequestTypeDAO(_Session session) {
		super(RequestType.class, session);
	}

}
