package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.PropertyCode;

import java.util.UUID;

public class PropertyCodeDAO extends ReferenceDAO<PropertyCode, UUID> {

    public PropertyCodeDAO(_Session session) throws DAOException {
        super(PropertyCode.class, session);
    }

}
