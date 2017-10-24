package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.RevenueCategory;
import reference.model.ЕxpenditurеCategory;

import java.util.UUID;

public class ЕxpenditurеCategoryDAO extends ReferenceDAO<ЕxpenditurеCategory, UUID> {

	public ЕxpenditurеCategoryDAO(_Session session) throws DAOException {
		super(ЕxpenditurеCategory.class, session);
	}
	
}
