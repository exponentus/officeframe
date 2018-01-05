package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.MemorialType;

import java.util.UUID;

public class MemorialTypeDAO extends ReferenceDAO<MemorialType, UUID> {

    public MemorialTypeDAO(_Session session) throws DAOException {
        super(MemorialType.class, session);
    }
}
