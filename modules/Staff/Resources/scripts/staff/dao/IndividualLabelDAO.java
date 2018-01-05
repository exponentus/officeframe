package staff.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import staff.model.IndividualLabel;

import java.util.UUID;

public class IndividualLabelDAO extends DAO<IndividualLabel, UUID> {

    public IndividualLabelDAO(_Session session) throws DAOException {
        super(IndividualLabel.class, session);
    }
}
