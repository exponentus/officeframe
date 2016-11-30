package staff.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.scripting._Session;

import staff.model.Department;

public class DepartmentDAO extends DAO<Department, UUID> {
	
	public DepartmentDAO(_Session session) throws DAOException {
		super(Department.class, session);
	}
	
}
