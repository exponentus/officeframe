package reference.dao;

import java.util.UUID;

import kz.lof.scripting._Session;
import reference.model.DepartmentType;

public class DepartmentTypeDAO extends ReferenceDAO<DepartmentType, UUID> {

	public DepartmentTypeDAO(_Session session) {
		super(DepartmentType.class, session);
	}

}
