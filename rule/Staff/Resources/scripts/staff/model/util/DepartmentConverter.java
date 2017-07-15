package staff.model.util;

import com.exponentus.common.model.converter.EntityConverter;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.user.AnonymousUser;
import org.eclipse.persistence.sessions.Session;
import staff.dao.DepartmentDAO;

import java.util.UUID;

public class DepartmentConverter extends EntityConverter {
	private static final long serialVersionUID = 1L;

	@Override
	public Object convertDataValueToObjectValue(Object dataValue, Session session) {
		if (dataValue != null) {
			try {
				DepartmentDAO dao = new DepartmentDAO(new _Session(new AnonymousUser()));
				return dao.findById((UUID) dataValue);
			} catch (DAOException e) {
				Server.logger.exception(e);
			} catch (Exception e) {
				Server.logger.exception(e);
			}
		}
		return null;

	}

}
