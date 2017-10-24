package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.BusClassification;
import reference.model.RouteClassification;

import java.util.UUID;

public class BusClassificationDAO extends ReferenceDAO<BusClassification, UUID> {

	public BusClassificationDAO(_Session session) throws DAOException {
		super(BusClassification.class, session);
	}
	
}
