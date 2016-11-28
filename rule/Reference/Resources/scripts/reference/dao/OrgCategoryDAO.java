package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.OrgCategory;

public class OrgCategoryDAO extends ReferenceDAO<OrgCategory, UUID> {
	
	public OrgCategoryDAO(_Session session) throws DAOException {
		super(OrgCategory.class, session);
	}
	
}
