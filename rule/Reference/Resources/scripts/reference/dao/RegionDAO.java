package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Region;

import java.util.UUID;

public class RegionDAO extends ReferenceDAO<Region, UUID> {

    public RegionDAO(_Session session) throws DAOException {
        super(Region.class, session);
    }
}
