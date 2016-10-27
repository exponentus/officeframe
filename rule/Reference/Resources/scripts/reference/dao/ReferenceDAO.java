package reference.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.localization.LanguageCode;
import com.exponentus.runtimeobj.IAppEntity;
import com.exponentus.scripting._Session;

/**
 * 
 * @author Kayra 10-01-2016
 *
 *         Common superclass for all entities of the Reference module. To more
 *         fine tuning reload it
 *
 */
public abstract class ReferenceDAO<T extends IAppEntity, K> extends DAO<T, K> {

	public ReferenceDAO(Class<T> entityClass, _Session session) {
		super(entityClass, session);
	}

	public T findByName(String name) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
			Root<T> c = cq.from(getEntityClass());
			cq.select(c);
			Predicate condition = cb.equal(cb.lower(c.<String> get("name")), name.toLowerCase());
			cq.where(condition);
			TypedQuery<T> typedQuery = em.createQuery(cq);
			return typedQuery.getSingleResult();
		} catch (NonUniqueResultException e) {
			return null;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	// TODO to implement
	public T findByName(String name, LanguageCode lang) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
			Root<T> c = cq.from(getEntityClass());
			cq.select(c);
			/*
			 * MapJoin p = c.joinMap("localizedName"); cq.multiselect(c,
			 * p.value()); cq.where(cb.equal(p.key(), lang));
			 */

			TypedQuery<T> typedQuery = em.createQuery(cq);
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public T findByCode(Enum<?> code) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			String jpql = "SELECT m FROM " + getEntityClass().getName() + " AS m WHERE m.code = :code";
			TypedQuery<T> q = em.createQuery(jpql, getEntityClass());
			q.setParameter("code", code);
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public ViewPage<T> findAllByKeyword(String keyword, int pageNum, int pageSize) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
			CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
			Root<T> c = cq.from(getEntityClass());
			cq.select(c);
			countCq.select(cb.count(c));
			if (!keyword.isEmpty()) {
				Predicate condition = cb.like(cb.lower(c.<String> get("name")), "%" + keyword.toLowerCase() + "%");
				cq.where(condition);
				countCq.where(condition);
			}
			TypedQuery<T> typedQuery = em.createQuery(cq);
			Query query = em.createQuery(countCq);
			long count = (long) query.getSingleResult();
			int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
			if (pageNum == 0) {
				pageNum = maxPage;
			}
			int firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
			typedQuery.setFirstResult(firstRec);
			typedQuery.setMaxResults(pageSize);
			List<T> result = typedQuery.getResultList();
			return new ViewPage<>(result, count, maxPage, pageNum);
		} finally {
			em.close();
		}
	}
}
