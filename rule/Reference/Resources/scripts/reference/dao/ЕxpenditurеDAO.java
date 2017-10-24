package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Еxpenditurе;
import reference.model.ЕxpenditurеCategory;

import java.util.UUID;

public class ЕxpenditurеDAO extends ReferenceDAO<Еxpenditurе, UUID> {

	public ЕxpenditurеDAO(_Session session) throws DAOException {
		super(Еxpenditurе.class, session);
	}
	
}
