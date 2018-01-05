package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.IndustryType;

import java.util.UUID;

public class IndustryTypeDAO extends ReferenceDAO<IndustryType, UUID> {

    public IndustryTypeDAO(_Session session) throws DAOException {
        super(IndustryType.class, session);
    }

}
