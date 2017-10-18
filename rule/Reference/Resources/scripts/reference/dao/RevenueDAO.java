package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Revenue;

import java.util.UUID;

public class RevenueDAO extends ReferenceDAO<Revenue, UUID> {

	public RevenueDAO(_Session session) throws DAOException {
		super(Revenue.class, session);
	}
	
}
