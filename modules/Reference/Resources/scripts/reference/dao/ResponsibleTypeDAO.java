package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.ResponsibleType;

import java.util.UUID;

public class ResponsibleTypeDAO extends ReferenceDAO<ResponsibleType, UUID> {

    public ResponsibleTypeDAO(_Session session) throws DAOException {
        super(ResponsibleType.class, session);
    }

}
