package reference.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.scripting._Session;

import reference.model.Tag;

public class TagDAO extends DAO<Tag, UUID> {

	public TagDAO(_Session session) throws DAOException {
		super(Tag.class, session);
	}

	public Tag findByName(String tagName) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
			Root<Tag> c = cq.from(Tag.class);
			cq.select(c);
			Predicate condition = cb.equal(c.get("name"), tagName);
			cq.where(condition);
			Query query = em.createQuery(cq);
			Tag entity = (Tag) query.getSingleResult();
			return entity;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Tag> findAllCategories() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query query = em.createQuery("SELECT e.category FROM Tag AS e GROUP BY e.category");
			return query.getResultList();
		} finally {
			em.close();
		}
	}

}
