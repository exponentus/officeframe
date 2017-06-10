package staff.domain;

import com.exponentus.common.domain.DTOService;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;

import staff.dao.EmployeeDAO;
import staff.model.Employee;

public class EmployeeDomain extends DTOService<Employee> {

	protected EmployeeDomain(_Session ses) throws DAOException {
		super(ses);
		dao = new EmployeeDAO(ses);
	}

	@Override
	public Employee fillFromDto(Employee dto, IValidation<Employee> validation, String formSesId) throws DTOException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
