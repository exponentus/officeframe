package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;
import reference.model.DepartmentType;

public class DepartmentTypeDAO extends ReferenceDAO<DepartmentType, UUID> {

	public DepartmentTypeDAO(_Session session) {
		super(DepartmentType.class, session);
	}

}
