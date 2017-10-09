package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.ActivityType;
import reference.model.Country;

import java.util.UUID;

public class ActivityTypeDAO extends ReferenceDAO<ActivityType, UUID> {

	public ActivityTypeDAO(_Session session) throws DAOException {
		super(ActivityType.class, session);
	}
	
}
