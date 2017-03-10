package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.WorkType;

public class WorkTypeDAO extends ReferenceDAO<WorkType, UUID> {

	public WorkTypeDAO(_Session session) throws DAOException {
		super(WorkType.class, session);
	}

}
