package calendar.domain;

import calendar.dao.ReminderDAO;
import calendar.model.Reminder;
import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;

public class ReminderDomain extends CommonDomain<Reminder> {

    public ReminderDomain(_Session ses) throws DAOException {
        super(ses);
        dao = new ReminderDAO(ses);
    }

    @Override
    public Reminder fillFromDto(Reminder dto, IValidation<Reminder> validation, String fsid) throws DTOException, DAOException {
        validation.check(dto);

        Reminder entity;

        if (dto.isNew()) {
            entity = new Reminder();
        } else {
            entity = dao.findById(dto.getId());
        }

        entity.setTitle(dto.getTitle());
        entity.setReminderType(dto.getReminderType());
        entity.setObservers(dto.getObservers());
        entity.setDescription(dto.getDescription());


        if (entity.isNew()) {
            entity.setAuthor(ses.getUser());
        }
        entity.setAttachments(getActualAttachments(entity.getAttachments(), dto.getAttachments(), fsid));
        calculateReadersEditors(entity);
        return entity;
    }

}
