package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.OperativeInfoType;
import reference.model.Position;

import java.util.UUID;

public class OperativeInfoTypeDAO extends ReferenceDAO<OperativeInfoType, UUID> {

    public OperativeInfoTypeDAO(_Session session) throws DAOException {
        super(OperativeInfoType.class, session);
    }

}
