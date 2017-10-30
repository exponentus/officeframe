package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.IndustryTypeCategory;

import java.util.UUID;

public class IndustryTypeCategoryDAO extends ReferenceDAO<IndustryTypeCategory, UUID> {

    public IndustryTypeCategoryDAO(_Session session) throws DAOException {
        super(IndustryTypeCategory.class, session);
    }

}
