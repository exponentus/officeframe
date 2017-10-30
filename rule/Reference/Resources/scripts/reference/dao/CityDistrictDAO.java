package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.CityDistrict;

import java.util.UUID;

public class CityDistrictDAO extends ReferenceDAO<CityDistrict, UUID> {

    public CityDistrictDAO(_Session session) throws DAOException {
        super(CityDistrict.class, session);
    }

}
