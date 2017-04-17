package staff.model.util;

import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.exponentus.dataengine.system.IExtUserDAO;
import com.exponentus.env.Environment;

import staff.model.Employee;

@Converter(autoApply = true)
public class EmployeeConvertor implements AttributeConverter<Employee, String> {

	@Override
	public String convertToDatabaseColumn(Employee entity) {
		return entity.getIdentifier();
	}

	@Override
	public Employee convertToEntityAttribute(String uuid) {
		IExtUserDAO dao = Environment.getExtUserDAO();
		return (Employee) dao.getEmployee(UUID.fromString(uuid));
	}

}