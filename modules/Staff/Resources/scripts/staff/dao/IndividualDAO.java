package staff.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import staff.model.Individual;

import java.util.UUID;

public class IndividualDAO extends DAO<Individual, UUID> {

    public IndividualDAO(_Session session) throws DAOException {
        super(Individual.class, session);
    }
}
