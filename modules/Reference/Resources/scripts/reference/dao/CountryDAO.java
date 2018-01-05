package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Country;
import reference.model.constants.CountryCode;

import java.util.UUID;

public class CountryDAO extends ReferenceDAO<Country, UUID> {

    public CountryDAO(_Session session) throws DAOException {
        super(Country.class, session);
    }

    public Country findByIdentifier(String identifier) {
        return findByCode(CountryCode.valueOf(identifier));
    }
}
