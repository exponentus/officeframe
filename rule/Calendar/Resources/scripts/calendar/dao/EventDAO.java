package calendar.dao;

import calendar.model.Event;
import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import java.util.UUID;

public class EventDAO extends DAO<Event, UUID> {

    public EventDAO(_Session session) throws DAOException {
        super(Event.class, session);
    }

}
