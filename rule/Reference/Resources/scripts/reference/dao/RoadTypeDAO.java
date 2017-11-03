package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Revenue;
import reference.model.RoadType;

import java.util.UUID;

public class RoadTypeDAO extends ReferenceDAO<RoadType, UUID> {

    public RoadTypeDAO(_Session session) throws DAOException {
        super(RoadType.class, session);
    }

}
