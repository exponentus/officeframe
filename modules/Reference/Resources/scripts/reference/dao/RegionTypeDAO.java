package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.RegionType;

import java.util.UUID;

public class RegionTypeDAO extends ReferenceDAO<RegionType, UUID> {

    public RegionTypeDAO(_Session session) throws DAOException {
        super(RegionType.class, session);
    }

}
