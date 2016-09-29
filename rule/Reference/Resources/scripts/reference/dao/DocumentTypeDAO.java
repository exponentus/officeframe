package reference.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting._Session;

import reference.model.DocumentType;

public class DocumentTypeDAO extends ReferenceDAO<DocumentType, UUID> {

	public DocumentTypeDAO(_Session session) {
		super(DocumentType.class, session);
	}

	public ViewPage<DocumentType> findAllCategories() {
		return findAllCategories(0, 0);
	}

	public ViewPage<DocumentType> findAllCategories(int pageNum, int pageSize) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<DocumentType> cq = cb.createQuery(getEntityClass());
			CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
			Root<DocumentType> c = cq.from(getEntityClass());
			cq.select(c);
			countCq.select(cb.count(c));
			// Predicate condition = cb.like(cb.lower(c.<String>
			// get("category")), "%" + category.toLowerCase() + "%");
			// cq.where(condition);
			// countCq.where(condition);
			TypedQuery<DocumentType> typedQuery = em.createQuery(cq);
			Query query = em.createQuery(countCq);
			long count = (long) query.getSingleResult();
			int maxPage = 1;
			if (pageNum != 0 || pageSize != 0) {
				maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
				if (pageNum == 0) {
					pageNum = maxPage;
				}
				int firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
				typedQuery.setFirstResult(firstRec);
				typedQuery.setMaxResults(pageSize);
			}
			List<DocumentType> result = typedQuery.getResultList();
			return new ViewPage<>(result, count, maxPage, pageNum);
		} finally {
			em.close();
		}
	}
}
