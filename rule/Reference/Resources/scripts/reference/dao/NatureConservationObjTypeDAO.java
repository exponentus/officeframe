package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.EngInfrastructObjClassification;
import reference.model.NatureConservationObjType;

import java.util.UUID;

public class NatureConservationObjTypeDAO extends ReferenceDAO<NatureConservationObjType, UUID> {

    public NatureConservationObjTypeDAO(_Session session) throws DAOException {
        super(NatureConservationObjType.class, session);
    }

}
