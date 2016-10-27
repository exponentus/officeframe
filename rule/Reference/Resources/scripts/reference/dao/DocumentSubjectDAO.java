package reference.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.exponentus.scripting._Session;

import reference.model.DocumentSubject;

public class DocumentSubjectDAO extends ReferenceDAO<DocumentSubject, UUID> {

	public DocumentSubjectDAO(_Session session) {
		super(DocumentSubject.class, session);
	}

	public List<DocumentSubject> findAllCategories() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query query = em.createQuery("SELECT e.category FROM DocumentSubject AS e GROUP BY e.category");
			return query.getResultList();
		} finally {
			em.close();
		}
	}

}
