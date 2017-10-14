package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.CashInflowCategory;

import java.util.UUID;

public class CashInflowCategoryDAO extends ReferenceDAO<CashInflowCategory, UUID> {

	public CashInflowCategoryDAO(_Session session) throws DAOException {
		super(CashInflowCategory.class, session);
	}
	
}
