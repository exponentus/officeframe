package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.BuildingMaterial;

import java.util.UUID;

public class BuildingMaterialDAO extends ReferenceDAO<BuildingMaterial, UUID> {

    public BuildingMaterialDAO(_Session session) throws DAOException {
        super(BuildingMaterial.class, session);
    }

}
