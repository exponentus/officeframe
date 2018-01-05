package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.TaskType;

import java.util.UUID;

public class TaskTypeDAO extends ReferenceDAO<TaskType, UUID> {

    public TaskTypeDAO(_Session session) throws DAOException {
        super(TaskType.class, session);
    }

}
