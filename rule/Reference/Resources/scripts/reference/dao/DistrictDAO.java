package reference.dao;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.District;
import reference.model.Region;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

public class DistrictDAO extends ReferenceDAO<District, UUID> {

	public DistrictDAO(_Session session) throws DAOException {
		super(District.class, session);
	}

	public ViewPage<District> findAllByRegion(Region region, int pageNum, int pageSize) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<District> cq = cb.createQuery(getEntityClass());
			CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
			Root<District> c = cq.from(getEntityClass());
			cq.select(c);
			countCq.select(cb.count(c));
			Predicate condition = cb.equal(c.get("region"), region);
			cq.where(condition);
			countCq.where(condition);

			TypedQuery<District> typedQuery = em.createQuery(cq);
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
			List<District> result = typedQuery.getResultList();
			return new ViewPage<District>(result, count, maxPage, pageNum);
		} finally {
			em.close();
		}
	}
}
