package reference.model.constants.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import reference.model.constants.RegionCode;

@Converter(autoApply = true)
public class RegionTypeConverter implements AttributeConverter<RegionCode, Integer> {

	@Override
	public Integer convertToDatabaseColumn(RegionCode lt) {
		return lt.getCode();
	}

	@Override
	public RegionCode convertToEntityAttribute(Integer lt) {
		return RegionCode.getType(lt);
	}
}
