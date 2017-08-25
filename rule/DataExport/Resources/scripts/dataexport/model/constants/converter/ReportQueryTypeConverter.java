package dataexport.model.constants.converter;

import com.exponentus.log.Lg;
import dataexport.model.constants.ReportQueryType;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ReportQueryTypeConverter implements AttributeConverter<ReportQueryType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ReportQueryType t) {
		return t.getCode();
	}

	@Override
	public ReportQueryType convertToEntityAttribute(Integer v) {
		try{
		return ReportQueryType.getType(v);
	}catch (Exception e){
		Lg.error(this.getClass().getSimpleName(), ExceptionUtils.getRootCauseMessage(e));
		return ReportQueryType.UNKNOWN;
	}

	}
}
