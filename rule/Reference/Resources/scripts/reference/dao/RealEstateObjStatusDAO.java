package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.DocumentType;
import reference.model.RealEstateObjStatus;

import java.util.UUID;

public class RealEstateObjStatusDAO extends ReferenceDAO<RealEstateObjStatus, UUID> {

	public RealEstateObjStatusDAO(_Session session) throws DAOException {
		super(RealEstateObjStatus.class, session);
	}
	
}
