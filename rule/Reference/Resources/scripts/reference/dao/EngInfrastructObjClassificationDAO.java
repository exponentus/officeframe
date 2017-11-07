package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.EngInfrastructObjClassification;

import java.util.UUID;

public class EngInfrastructObjClassificationDAO extends ReferenceDAO<EngInfrastructObjClassification, UUID> {

    public EngInfrastructObjClassificationDAO(_Session session) throws DAOException {
        super(EngInfrastructObjClassification.class, session);
    }

}
