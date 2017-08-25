package dataexport.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import dataexport.model.ReportProfile;

import java.util.UUID;

public class ReportProfileDAO extends DAO<ReportProfile, UUID> {

    public ReportProfileDAO(_Session session) throws DAOException {
        super(ReportProfile.class, session);
    }
}
