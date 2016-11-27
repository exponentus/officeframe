package reference.dao;

import com.exponentus.scripting._Session;
import reference.model.DocumentTypeCategory;

import java.util.UUID;

public class DocumentTypeCategoryDAO extends ReferenceDAO<DocumentTypeCategory, UUID> {

	public DocumentTypeCategoryDAO(_Session session) {
		super(DocumentTypeCategory.class, session);
	}

}
