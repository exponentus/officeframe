package staff.model.util;

import java.util.UUID;

import org.eclipse.persistence.sessions.Session;

import com.exponentus.common.model.util.EntityConverter;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.user.AnonymousUser;

import staff.dao.DepartmentDAO;

public class DepartmentConverter extends EntityConverter {
	private static final long serialVersionUID = 1L;

	@Override
	public Object convertDataValueToObjectValue(Object dataValue, Session session) {
		if (dataValue != null) {
			try {
				DepartmentDAO dao = new DepartmentDAO(new _Session(new AnonymousUser()));
				return dao.findById((UUID) dataValue);
			} catch (DAOException e) {
				Server.logger.errorLogEntry(e);
			} catch (Exception e) {
				Server.logger.errorLogEntry(e);
			}
		}
		return null;

	}

}
