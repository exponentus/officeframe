package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.DocumentSubject;

import java.util.UUID;

public class DocumentSubjectDAO extends ReferenceDAO<DocumentSubject, UUID> {

    public DocumentSubjectDAO(_Session session) throws DAOException {
        super(DocumentSubject.class, session);
    }

}
