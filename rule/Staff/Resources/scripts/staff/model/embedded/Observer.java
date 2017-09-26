package staff.model.embedded;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import staff.model.Employee;
import staff.model.util.EmployeeConverter;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Embeddable
@Converter(name = "emp_conv", converterClass = EmployeeConverter.class)
public class Observer {

    @Convert("emp_conv")
    @Basic(optional = true)
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String toString() {
        return "employee=" + employee;
    }
}
