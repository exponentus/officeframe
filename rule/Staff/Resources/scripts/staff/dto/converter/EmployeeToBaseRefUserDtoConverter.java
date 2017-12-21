package staff.dto.converter;

import com.exponentus.common.converter.GenericConverter;
import com.exponentus.common.ui.BaseReferenceModel;
import staff.model.Employee;

public class EmployeeToBaseRefUserDtoConverter implements GenericConverter<Employee, BaseReferenceModel<Long>> {

    @Override
    public BaseReferenceModel<Long> apply(Employee entity) {
        return new BaseReferenceModel<>(entity.getUserID(), entity.getName());
    }
}
