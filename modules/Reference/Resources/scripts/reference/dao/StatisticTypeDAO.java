package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.StatisticType;

import java.util.UUID;

public class StatisticTypeDAO extends ReferenceDAO<StatisticType, UUID> {

    public StatisticTypeDAO(_Session session) throws DAOException {
        super(StatisticType.class, session);
    }
}
