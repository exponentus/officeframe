package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.RealEstateObjPurpose;

import java.util.UUID;

public class RealEstateObjPurposeDAO extends ReferenceDAO<RealEstateObjPurpose, UUID> {

    public RealEstateObjPurposeDAO(_Session session) throws DAOException {
        super(RealEstateObjPurpose.class, session);
    }

}
