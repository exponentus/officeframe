package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.StructureType;

import java.util.UUID;

public class StructureTypeDAO extends ReferenceDAO<StructureType, UUID> {

    public StructureTypeDAO(_Session session) throws DAOException {
        super(StructureType.class, session);
    }

}
