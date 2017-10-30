package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.LocalityType;

import java.util.UUID;

public class LocalityTypeDAO extends ReferenceDAO<LocalityType, UUID> {

    public LocalityTypeDAO(_Session session) throws DAOException {
        super(LocalityType.class, session);
    }

}
