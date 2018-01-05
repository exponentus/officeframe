package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.ActivityTypeCategory;

import java.util.UUID;

public class ActivityTypeCategoryDAO extends ReferenceDAO<ActivityTypeCategory, UUID> {

    public ActivityTypeCategoryDAO(_Session session) throws DAOException {
        super(ActivityTypeCategory.class, session);
    }

}
