package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Position;

import java.util.UUID;

public class PositionDAO extends ReferenceDAO<Position, UUID> {

    public PositionDAO(_Session session) throws DAOException {
        super(Position.class, session);
    }

}
