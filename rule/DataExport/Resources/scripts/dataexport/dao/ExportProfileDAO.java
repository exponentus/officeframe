package dataexport.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import dataexport.model.ExportProfile;

import java.util.UUID;

public class ExportProfileDAO extends DAO<ExportProfile, UUID> {

	public ExportProfileDAO(_Session session) throws DAOException {
		super(ExportProfile.class, session);
	}

}
