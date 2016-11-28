package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.PropertyCode;

public class PropertyCodeDAO extends ReferenceDAO<PropertyCode, UUID> {
	
	public PropertyCodeDAO(_Session session) throws DAOException {
		super(PropertyCode.class, session);
	}
	
}
