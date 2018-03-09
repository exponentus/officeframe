package reference.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.AsOf;

import java.util.UUID;

public class AsOfDAO extends DAO<AsOf, UUID> {

    public AsOfDAO(_Session session) throws DAOException {
        super(AsOf.class, session);
    }
}
