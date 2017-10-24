package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Expenditure;

import java.util.UUID;

public class ExpenditureDAO  extends ReferenceDAO<Expenditure, UUID> {

	public ExpenditureDAO(_Session session) throws DAOException {
		super(Expenditure.class, session);
	}
	
}

