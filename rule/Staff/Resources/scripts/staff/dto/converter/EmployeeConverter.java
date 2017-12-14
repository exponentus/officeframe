package staff.dto.converter;

import administrator.model.User;
import com.exponentus.common.converter.GenericConverter;
import reference.model.Position;
import staff.model.Employee;

import java.util.List;

public class EmployeeConverter implements GenericConverter<Employee, Employee> {

    private List<String> fields;

    public EmployeeConverter() {
    }

    public EmployeeConverter(List<String> fields) {
        if (fields != null && fields.size() > 0 && !fields.get(0).isEmpty()) {
            this.fields = fields;
        }
    }

    @Override
    public Employee apply(Employee emp) {
        Employee dto = new Employee();

        dto.setId(emp.getId());
        dto.setTitle(emp.getTitle());
        dto.setName(emp.getName());

        if (fields != null) {
            for (String fieldName : fields) {

            }
        } else {
            User user = new User();
            user.setLogin(emp.getUser().getLogin());
            dto.setUser(user);
            if (emp.getPosition() != null) {
                Position position = new Position();
                position.setTitle(emp.getPosition().getTitle());
                position.setLocName(emp.getPosition().getLocName());
                dto.setPosition(position);
            }
            dto.setAvatar(emp.getAvatar());
        }

        return dto;
    }
}
