package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.DocumentLanguage;

import java.util.UUID;

public class DocumentLanguageDAO extends ReferenceDAO<DocumentLanguage, UUID> {

    public DocumentLanguageDAO(_Session session) throws DAOException {
        super(DocumentLanguage.class, session);
    }

}
