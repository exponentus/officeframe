package staff.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import staff.model.Department;

import java.util.UUID;

public class DepartmentDAO extends DAO<Department, UUID> {

    public DepartmentDAO(_Session session) throws DAOException {
        super(Department.class, session);
    }

}
