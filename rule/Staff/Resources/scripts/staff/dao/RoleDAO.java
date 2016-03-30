package staff.dao;

import java.util.UUID;

import kz.lof.dataengine.jpa.DAO;
import kz.lof.scripting._Session;
import staff.model.Role;

public class RoleDAO extends DAO<Role, UUID> {

	public RoleDAO(_Session session) {
		super(Role.class, session);
	}

}
