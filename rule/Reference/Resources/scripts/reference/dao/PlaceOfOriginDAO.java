package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.PlaceOfOrigin;

public class PlaceOfOriginDAO extends ReferenceDAO<PlaceOfOrigin, UUID> {

	public PlaceOfOriginDAO(_Session session) throws DAOException {
		super(PlaceOfOrigin.class, session);
	}

}
