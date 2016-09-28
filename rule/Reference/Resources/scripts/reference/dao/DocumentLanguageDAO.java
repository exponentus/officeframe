package reference.dao;

import java.util.UUID;

import com.exponentus.scripting._Session;

import reference.model.DocumentLanguage;

public class DocumentLanguageDAO extends ReferenceDAO<DocumentLanguage, UUID> {

	public DocumentLanguageDAO(_Session session) {
		super(DocumentLanguage.class, session);
	}

}
