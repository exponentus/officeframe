package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.BuildingState;

import java.util.UUID;

public class BuildingStateDAO extends ReferenceDAO<BuildingState, UUID> {

	public BuildingStateDAO(_Session session) throws DAOException {
		super(BuildingState.class, session);
	}
	
}
