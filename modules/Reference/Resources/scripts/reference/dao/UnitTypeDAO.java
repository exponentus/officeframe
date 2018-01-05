package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.UnitType;

import java.util.UUID;

public class UnitTypeDAO extends ReferenceDAO<UnitType, UUID> {

    public UnitTypeDAO(_Session session) throws DAOException {
        super(UnitType.class, session);
    }
}
