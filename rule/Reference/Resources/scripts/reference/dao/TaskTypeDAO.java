package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;

import reference.model.TaskType;

public class TaskTypeDAO extends ReferenceDAO<TaskType, UUID> {

	public TaskTypeDAO(_Session session) {
		super(TaskType.class, session);
	}

}
