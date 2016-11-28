package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.DocumentTypeCategory;

public class DocumentTypeCategoryDAO extends ReferenceDAO<DocumentTypeCategory, UUID> {
	
	public DocumentTypeCategoryDAO(_Session session) throws DAOException {
		super(DocumentTypeCategory.class, session);
	}
	
}
