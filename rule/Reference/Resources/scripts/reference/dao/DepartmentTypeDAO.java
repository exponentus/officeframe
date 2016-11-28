package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.DepartmentType;

public class DepartmentTypeDAO extends ReferenceDAO<DepartmentType, UUID> {
	
	public DepartmentTypeDAO(_Session session) throws DAOException {
		super(DepartmentType.class, session);
	}
	
}
