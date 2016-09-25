package discussing.dao;

import java.util.UUID;

import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.scripting._Session;

import dataexport.model.ExportProfile;

public class ExportProfileDAO extends DAO<ExportProfile, UUID> {

	public ExportProfileDAO(_Session session) {
		super(ExportProfile.class, session);
	}

}
