package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.TaxIncome;
import reference.model.TaxIncomeCategory;

import java.util.UUID;

public class TaxIncomeDAO extends ReferenceDAO<TaxIncome, UUID> {

	public TaxIncomeDAO(_Session session) throws DAOException {
		super(TaxIncome.class, session);
	}
	
}
