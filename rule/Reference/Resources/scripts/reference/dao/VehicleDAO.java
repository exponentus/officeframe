package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.Vehicle;

public class VehicleDAO extends ReferenceDAO<Vehicle, UUID> {

	public VehicleDAO(_Session session) throws DAOException {
		super(Vehicle.class, session);
	}

}
