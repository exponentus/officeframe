package reference.dao;

import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.scripting._Session;
import reference.model.DemandType;

import java.util.UUID;

public class DemandTypeDAO extends DAO<DemandType, UUID> {

    public DemandTypeDAO(_Session session) {
        super(DemandType.class, session);
    }
}
