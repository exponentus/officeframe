package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.RevenueCategory;

import java.util.UUID;

public class RevenueCategoryDAO extends ReferenceDAO<RevenueCategory, UUID> {

	public RevenueCategoryDAO(_Session session) throws DAOException {
		super(RevenueCategory.class, session);
	}
	
}
