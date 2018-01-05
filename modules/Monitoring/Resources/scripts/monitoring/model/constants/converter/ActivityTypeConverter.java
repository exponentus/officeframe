package monitoring.model.constants.converter;

import monitoring.model.constants.ActivityType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ActivityTypeConverter implements AttributeConverter<ActivityType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ActivityType issuePriority) {
        return issuePriority.getCode();
    }

    @Override
    public ActivityType convertToEntityAttribute(Integer priorityValue) {
        return ActivityType.getType(priorityValue);
    }
}
