package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.ProductType;
import reference.model.ProjectStage;

import java.util.UUID;

public class ProjectStageDAO extends ReferenceDAO<ProjectStage, UUID> {

    public ProjectStageDAO(_Session session) throws DAOException {
        super(ProjectStage.class, session);
    }
}
