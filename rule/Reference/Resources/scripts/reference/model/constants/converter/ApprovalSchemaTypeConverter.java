package reference.model.constants.converter;

import com.exponentus.log.Lg;
import org.apache.commons.lang3.exception.ExceptionUtils;
import reference.model.constants.ApprovalSchemaType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ApprovalSchemaTypeConverter implements AttributeConverter<ApprovalSchemaType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ApprovalSchemaType t) {
		return t.getCode();
	}

	@Override
	public ApprovalSchemaType convertToEntityAttribute(Integer v) {
		try{
		return ApprovalSchemaType.getType(v);
	}catch (Exception e){
		Lg.error(this.getClass().getSimpleName(), ExceptionUtils.getRootCauseMessage(e));
		return ApprovalSchemaType.UNKNOWN;
	}

	}
}
