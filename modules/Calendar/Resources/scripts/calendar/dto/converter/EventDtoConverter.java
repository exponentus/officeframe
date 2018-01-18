package calendar.dto.converter;

import calendar.model.Event;
import com.exponentus.common.converter.GenericConverter;

public class EventDtoConverter implements GenericConverter<Event, Event> {

    @Override
    public Event apply(Event event) {
        Event dto = new Event();

        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setEventTime(event.getEventTime());
        dto.setPriority(event.getPriority());
        dto.setTags(event.getTags());

        return dto;
    }
}
