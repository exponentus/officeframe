package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.RouteClassification;
import reference.model.RouteStatus;

import java.util.UUID;

public class RouteStatusDAO extends ReferenceDAO<RouteStatus, UUID> {

	public RouteStatusDAO(_Session session) throws DAOException {
		super(RouteStatus.class, session);
	}
	
}
