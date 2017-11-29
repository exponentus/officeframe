package calendar.model.constants.converter;

import calendar.model.constants.ReminderType;
import com.exponentus.log.Lg;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ReminderTypeConverter implements AttributeConverter<ReminderType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ReminderType t) {
        return t.getCode();
    }

    @Override
    public ReminderType convertToEntityAttribute(Integer v) {
        try {
            return ReminderType.getType(v);
        } catch (Exception e) {
            Lg.error(this.getClass().getSimpleName(), ExceptionUtils.getRootCauseMessage(e));
            return ReminderType.UNKNOWN;
        }

    }
}
