package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Vehicle;

import java.util.UUID;

public class VehicleDAO extends ReferenceDAO<Vehicle, UUID> {

    public VehicleDAO(_Session session) throws DAOException {
        super(Vehicle.class, session);
    }
}
