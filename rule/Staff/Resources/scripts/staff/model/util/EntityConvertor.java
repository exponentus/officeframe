package staff.model.util;

import java.sql.Types;
import java.util.UUID;

import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IDAO;
import com.exponentus.runtimeobj.IAppEntity;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.user.AnonymousUser;

import staff.dao.DepartmentDAO;
import staff.dao.EmployeeDAO;

public class EntityConvertor implements Converter {
	private static final long serialVersionUID = 1L;
	private String fieldName;

	@Override
	public Object convertObjectValueToDataValue(Object objectValue, Session session) {
		if (objectValue != null) {
			return ((IAppEntity<UUID>) objectValue).getId();
		}

		return null;

	}

	// TODO it need to use Factory with aliases
	@Override
	public Object convertDataValueToObjectValue(Object dataValue, Session session) {
		// System.out.println("table=" + tableName);
		if (dataValue != null) {
			try {
				IDAO dao = null;
				if (fieldName.equalsIgnoreCase("employee") || fieldName.equalsIgnoreCase("boss")) {
					dao = new EmployeeDAO(new _Session(new AnonymousUser()));
				} else if (fieldName.equalsIgnoreCase("department") || fieldName.equalsIgnoreCase("leadDepartment")) {
					dao = new DepartmentDAO(new _Session(new AnonymousUser()));
				}
				return dao.findById((UUID) dataValue);
			} catch (DAOException e) {
				Server.logger.errorLogEntry(e);
			} catch (Exception e) {
				Server.logger.errorLogEntry(e);
			}
		}
		return null;

	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public void initialize(DatabaseMapping mapping, Session session) {
		DatabaseField field = mapping.getField();
		field.setSqlType(Types.OTHER);
		field.setTypeName("java.util.UUID");
		field.setColumnDefinition("uuid");
		fieldName = field.getName();
	}
}
