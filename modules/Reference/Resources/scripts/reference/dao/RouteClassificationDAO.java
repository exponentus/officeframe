package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.RouteClassification;

import java.util.UUID;

public class RouteClassificationDAO extends ReferenceDAO<RouteClassification, UUID> {

    public RouteClassificationDAO(_Session session) throws DAOException {
        super(RouteClassification.class, session);
    }

}
