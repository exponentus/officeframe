package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.TaskType;

public class TaskTypeDAO extends ReferenceDAO<TaskType, UUID> {
	
	public TaskTypeDAO(_Session session) throws DAOException {
		super(TaskType.class, session);
	}
	
}
