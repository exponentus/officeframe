package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;
import reference.model.OrgCategory;

public class OrgCategoryDAO extends ReferenceDAO<OrgCategory, UUID> {

	public OrgCategoryDAO(_Session session) {
		super(OrgCategory.class, session);
	}

}
