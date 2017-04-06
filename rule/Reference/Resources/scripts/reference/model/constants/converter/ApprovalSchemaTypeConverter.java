package reference.model.constants.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import reference.model.constants.ApprovalSchemaType;

@Converter(autoApply = true)
public class ApprovalSchemaTypeConverter implements AttributeConverter<ApprovalSchemaType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ApprovalSchemaType t) {
		return t.getCode();
	}

	@Override
	public ApprovalSchemaType convertToEntityAttribute(Integer v) {
		return ApprovalSchemaType.getType(v);
	}
}
