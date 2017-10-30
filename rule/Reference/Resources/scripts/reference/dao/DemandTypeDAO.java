package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.DemandType;

import java.util.UUID;

public class DemandTypeDAO extends ReferenceDAO<DemandType, UUID> {

    public DemandTypeDAO(_Session session) throws DAOException {
        super(DemandType.class, session);
    }
}
