package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.Country;

public class CountryDAO extends ReferenceDAO<Country, UUID> {
	
	public CountryDAO(_Session session) throws DAOException {
		super(Country.class, session);
	}
	
}
