package staff.model.embedded;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import staff.model.Employee;
import staff.model.util.EmployeeConverter;

@Embeddable
public class Observer {
	@Converter(name = "emp_conv", converterClass = EmployeeConverter.class)
	@Convert("emp_conv")
	@Basic(fetch = FetchType.LAZY, optional = true)
	private Employee employee;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
