package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.LandClassification;
import reference.model.Position;

import java.util.UUID;

public class LandClassificationDAO extends ReferenceDAO<LandClassification, UUID> {

	public LandClassificationDAO(_Session session) throws DAOException {
		super(LandClassification.class, session);
	}
	
}
