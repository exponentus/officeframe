package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.RequestType;

import java.util.UUID;

public class RequestTypeDAO extends ReferenceDAO<RequestType, UUID> {

    public RequestTypeDAO(_Session session) throws DAOException {
        super(RequestType.class, session);
    }

}
