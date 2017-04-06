package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.ApprovalRoute;

public class ApprovalRouteDAO extends ReferenceDAO<ApprovalRoute, UUID> {

	public ApprovalRouteDAO(_Session session) throws DAOException {
		super(ApprovalRoute.class, session);
	}

}
