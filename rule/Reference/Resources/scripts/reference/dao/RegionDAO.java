package reference.dao;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.Country;
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

public class RegionDAO extends ReferenceDAO<Region, UUID> {

    public RegionDAO(_Session session) throws DAOException {
        super(Region.class, session);
    }

    public Region findPrimary() {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery("SELECT e FROM " + getEntityClass().getName()
                    + " AS e WHERE e.isPrimary = TRUE");
            return (Region) query.getResultList().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public ViewPage<Region> findAllByCountry(Country country, int pageNum, int pageSize) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<Region> cq = cb.createQuery(getEntityClass());
            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<Region> c = cq.from(getEntityClass());
            cq.select(c);
            countCq.select(cb.count(c));
            Predicate condition = cb.equal(c.get("country"), country);
            cq.where(condition);
            countCq.where(condition);

            TypedQuery<Region> typedQuery = em.createQuery(cq);
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
            List<Region> result = typedQuery.getResultList();
            return new ViewPage<Region>(result, count, maxPage, pageNum);
        } finally {
            em.close();
        }
    }
}
