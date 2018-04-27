package staff.dto.converter;

import administrator.model.User;
import com.exponentus.common.dto.converter.GenericConverter;
import com.exponentus.common.model.embedded.Avatar;
import reference.model.Position;
import staff.model.Employee;

import java.util.List;

public class EmployeeDtoConverter implements GenericConverter<Employee, Employee> {

    private List<String> fields;

    public EmployeeDtoConverter() {
    }

    public EmployeeDtoConverter(List<String> fields) {
        if (fields != null && fields.size() > 0 && !fields.get(0).isEmpty()) {
            this.fields = fields;
        }
    }

    @Override
    public Employee apply(Employee entity) {
        Employee dto = new Employee();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setName(entity.getName());
        User user = new User();
        user.setId(entity.getUser().getId());
        user.setLogin(entity.getUser().getLogin());
        dto.setUser(user);

        if (fields != null) {
            for (String fieldName : fields) {

            }
            if (entity.getAvatar() != null) {
                dto.setAvatar(new Avatar());
            }
        } else {
            if (entity.getPosition() != null) {
                Position position = new Position();
                position.setTitle(entity.getPosition().getTitle());
                position.setLocName(entity.getPosition().getLocName());
                dto.setPosition(position);
            }
            dto.setRoles(entity.getRoles());
            dto.setAvatar(entity.getAvatar());
        }

        return dto;
    }
}
