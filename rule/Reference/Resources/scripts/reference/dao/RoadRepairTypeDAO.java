package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.RoadRepairType;

import java.util.UUID;

public class RoadRepairTypeDAO extends ReferenceDAO<RoadRepairType, UUID> {

    public RoadRepairTypeDAO(_Session session) throws DAOException {
        super(RoadRepairType.class, session);
    }

}
