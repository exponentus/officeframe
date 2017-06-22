package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.InvestedArea;

public class InvestedAreaDAO extends ReferenceDAO<InvestedArea, UUID> {

	public InvestedAreaDAO(_Session session) throws DAOException {
		super(InvestedArea.class, session);
	}

}
