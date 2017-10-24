package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.ExpenditureCategory;

import java.util.UUID;

public class ExpenditureCategoryDAO  extends ReferenceDAO<ExpenditureCategory, UUID> {

	public ExpenditureCategoryDAO(_Session session) throws DAOException {
		super(ExpenditureCategory.class, session);
	}
	
}
