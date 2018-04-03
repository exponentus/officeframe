package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.WayOfInteraction;

import java.util.UUID;

public class WayOfInteractionDAO extends ReferenceDAO<WayOfInteraction, UUID> {

    public WayOfInteractionDAO(_Session session) throws DAOException {
        super(WayOfInteraction.class, session);
    }

}
