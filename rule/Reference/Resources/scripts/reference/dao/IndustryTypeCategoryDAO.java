package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.IndustryTypeCategory;

public class IndustryTypeCategoryDAO extends ReferenceDAO<IndustryTypeCategory, UUID> {

	public IndustryTypeCategoryDAO(_Session session) throws DAOException {
		super(IndustryTypeCategory.class, session);
	}

}
