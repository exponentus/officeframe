package reference.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting._Session;

import reference.model.Locality;
import reference.model.Street;

public class StreetDAO extends ReferenceDAO<Street, UUID> {
	
	public StreetDAO(_Session session) throws DAOException {
		super(Street.class, session);
	}
	
	@Override
	public ViewPage<Street> findAllByKeyword(String keyword, int pageNum, int pageSize) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<Street> cq = cb.createQuery(getEntityClass());
			CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
			Root<Street> c = cq.from(getEntityClass());
			cq.select(c);
			countCq.select(cb.count(c));
			Predicate condition = cb.like(cb.lower(c.<String>get("name")), "%" + keyword.toLowerCase() + "%");
			cq.where(condition);
			countCq.where(condition);
			TypedQuery<Street> typedQuery = em.createQuery(cq);
			Query query = em.createQuery(countCq);
			long count = (long) query.getSingleResult();
			int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
			if (pageNum == 0) {
				pageNum = maxPage;
			}
			int firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
			typedQuery.setFirstResult(firstRec);
			typedQuery.setMaxResults(pageSize);
			List<Street> result = typedQuery.getResultList();
			return new ViewPage<Street>(result, count, maxPage, pageNum);
		} finally {
			em.close();
		}
	}
	
	public ViewPage<Street> findAllInLocalityByKeyword(Locality locality, String keyword, int pageNum, int pageSize) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<Street> cq = cb.createQuery(getEntityClass());
			CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
			Root<Street> c = cq.from(getEntityClass());
			cq.select(c);
			countCq.select(cb.count(c));
			Predicate condition = cb.equal(c.get("locality"), locality);
			condition = cb.and(cb.like(cb.lower(c.<String>get("name")), "%" + keyword.toLowerCase() + "%"), condition);
			cq.where(condition);
			countCq.where(condition);
			TypedQuery<Street> typedQuery = em.createQuery(cq);
			Query query = em.createQuery(countCq);
			long count = (long) query.getSingleResult();
			int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
			if (pageNum == 0) {
				pageNum = maxPage;
			}
			int firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
			typedQuery.setFirstResult(firstRec);
			typedQuery.setMaxResults(pageSize);
			List<Street> result = typedQuery.getResultList();
			return new ViewPage<Street>(result, count, maxPage, pageNum);
		} finally {
			em.close();
		}
	}
}
