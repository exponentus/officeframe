package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.DocumentType;

public class DocumentTypeDAO extends ReferenceDAO<DocumentType, UUID> {
	
	public DocumentTypeDAO(_Session session) throws DAOException {
		super(DocumentType.class, session);
	}
	
}
