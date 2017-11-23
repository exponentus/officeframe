package calendar.domain;

import calendar.dao.EventDAO;
import calendar.model.Event;
import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;

public class EventDomain extends CommonDomain<Event> {

    public EventDomain(_Session ses) throws DAOException {
        super(ses);
        dao = new EventDAO(ses);
    }

    @Override
    public Event fillFromDto(Event dto, IValidation<Event> validation, String fsid) throws DTOException, DAOException {
        validation.check(dto);

        Event entity;

        if (dto.isNew()) {
            entity = new Event();
        } else {
            entity = dao.findById(dto.getId());
        }

        entity.setTitle(dto.getTitle());
        entity.setEventTime(dto.getEventTime());
        entity.setObservers(dto.getObservers());
        entity.setDescription(dto.getDescription());
        entity.setPriority(dto.getPriority());
        entity.setReminder(dto.getReminder());
        entity.setTags(dto.getTags());

        if (entity.isNew()) {
            entity.setAuthor(ses.getUser());
        }
        entity.setAttachments(getActualAttachments(entity.getAttachments(), dto.getAttachments(), fsid));
        calculateReadersEditors(entity);
        return entity;
    }

}
