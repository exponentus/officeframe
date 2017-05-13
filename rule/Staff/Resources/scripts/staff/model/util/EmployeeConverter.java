package staff.model.util;

import java.util.UUID;

import org.eclipse.persistence.sessions.Session;

import com.exponentus.common.model.util.EntityConverter;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.user.AnonymousUser;

import staff.dao.EmployeeDAO;
import staff.model.Employee;

public class EmployeeConverter extends EntityConverter {
	private static final long serialVersionUID = 1L;

	@Override
	public Object convertDataValueToObjectValue(Object dataValue, Session session) {
		if (dataValue != null) {
			try {
				return (Employee) new EmployeeDAO(new _Session(new AnonymousUser())).findById((UUID) dataValue);
			} catch (DAOException e) {
				Server.logger.exception(e);
			} catch (Exception e) {
				Server.logger.exception(e);
			}
		}
		return null;

	}

}
