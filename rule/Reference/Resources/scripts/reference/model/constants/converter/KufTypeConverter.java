package reference.model.constants.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import reference.model.constants.KufType;

@Converter(autoApply = true)
public class KufTypeConverter implements AttributeConverter<KufType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(KufType issuePriority) {
		return issuePriority.getCode();
	}

	@Override
	public KufType convertToEntityAttribute(Integer priorityValue) {
		return KufType.getType(priorityValue);
	}
}
