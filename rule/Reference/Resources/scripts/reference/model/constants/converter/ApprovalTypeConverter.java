package reference.model.constants.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import reference.model.constants.ApprovalType;

@Converter(autoApply = true)
public class ApprovalTypeConverter implements AttributeConverter<ApprovalType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ApprovalType issuePriority) {
		return issuePriority.getCode();
	}

	@Override
	public ApprovalType convertToEntityAttribute(Integer priorityValue) {
		return ApprovalType.getType(priorityValue);
	}
}
