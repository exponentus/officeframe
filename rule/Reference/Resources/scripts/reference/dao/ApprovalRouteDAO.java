package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.ApprovalRoute;

import java.util.UUID;

public class ApprovalRouteDAO extends ReferenceDAO<ApprovalRoute, UUID> {

    public ApprovalRouteDAO(_Session session) throws DAOException {
        super(ApprovalRoute.class, session);
    }

}
