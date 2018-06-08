package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.StatisticIndicatorType;

import java.util.UUID;

public class StatisticIndicatorTypeDAO extends ReferenceDAO<StatisticIndicatorType, UUID> {

    public StatisticIndicatorTypeDAO(_Session session) throws DAOException {
        super(StatisticIndicatorType.class, session);
    }
}
