package reference.model.constants.converter;

import reference.model.constants.ControlSchemaType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ControlSchemaTypeConverter implements AttributeConverter<ControlSchemaType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ControlSchemaType t) {
        return t.getCode();
    }

    @Override
    public ControlSchemaType convertToEntityAttribute(Integer v) {
        return ControlSchemaType.getType(v);
    }
}
