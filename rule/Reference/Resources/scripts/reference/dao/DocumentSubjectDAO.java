package reference.dao;

import java.util.UUID;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;

import reference.model.DocumentSubject;

public class DocumentSubjectDAO extends ReferenceDAO<DocumentSubject, UUID> {

	public DocumentSubjectDAO(_Session session) throws DAOException {
		super(DocumentSubject.class, session);
	}
	
}
