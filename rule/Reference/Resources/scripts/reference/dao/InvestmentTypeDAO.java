package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.InvestmentType;

public class InvestmentTypeDAO extends ReferenceDAO<InvestmentType, UUID> {

	public InvestmentTypeDAO(_Session session) throws DAOException {
		super(InvestmentType.class, session);
	}

}
