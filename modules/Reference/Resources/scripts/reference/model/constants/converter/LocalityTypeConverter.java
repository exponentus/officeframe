package reference.model.constants.converter;

import reference.model.constants.LocalityCode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalityTypeConverter implements AttributeConverter<LocalityCode, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LocalityCode lt) {
        return lt.getCode();
    }

    @Override
    public LocalityCode convertToEntityAttribute(Integer lt) {
        return LocalityCode.getType(lt);
    }
}
