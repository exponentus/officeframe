package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.TaxIncomeCategory;

import java.util.UUID;

public class TaxIncomeCategoryDAO extends ReferenceDAO<TaxIncomeCategory, UUID> {

	public TaxIncomeCategoryDAO(_Session session) throws DAOException {
		super(TaxIncomeCategory.class, session);
	}
	
}
