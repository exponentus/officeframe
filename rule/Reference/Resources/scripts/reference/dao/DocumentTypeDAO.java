package reference.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.exponentus.scripting._Session;

import reference.model.DocumentType;

public class DocumentTypeDAO extends ReferenceDAO<DocumentType, UUID> {

	public DocumentTypeDAO(_Session session) {
		super(DocumentType.class, session);
	}

	public List<DocumentType> findAllCategories() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Query query = em.createQuery("SELECT e.category FROM DocumentType AS e GROUP BY e.category");
		return query.getResultList();
	}

}
