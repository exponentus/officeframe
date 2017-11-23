package calendar.dao;

import calendar.model.Reminder;
import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import java.util.UUID;

public class ReminderDAO extends DAO<Reminder, UUID> {

	public ReminderDAO(_Session session) throws DAOException {
		super(Reminder.class, session);
	}

}
