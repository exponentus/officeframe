package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.CashInflow;

import java.util.UUID;

public class CashInflowDAO extends ReferenceDAO<CashInflow, UUID> {

	public CashInflowDAO(_Session session) throws DAOException {
		super(CashInflow.class, session);
	}
	
}
