package reference.dao;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting._Session;
import reference.model.Tag;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

public class TagDAO extends ReferenceDAO<Tag, UUID> {

    public TagDAO(_Session session) throws DAOException {
        super(Tag.class, session);
    }

    public ViewPage<Tag> findAllByCategoryAndVisibility(String categoryName, boolean withHidden, int pageNum, int pageSize) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<Tag> c = cq.from(Tag.class);
            cq.select(c);
            countCq.select(cb.count(c));

            Predicate condition = null;

            if (!categoryName.isEmpty()) {
                condition = cb.or(cb.equal(c.get("category"), ""), cb.like(cb.lower(c.<String>get("category")), categoryName));
            }
            if (!withHidden) {
                if (condition == null) {
                    condition = cb.isFalse(c.get("hidden"));
                } else {
                    condition = cb.and(cb.isFalse(c.get("hidden")), condition);
                }
            }

            cq.where(condition);
            countCq.where(condition);
            TypedQuery<Tag> typedQuery = em.createQuery(cq);
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
            List<Tag> result = typedQuery.getResultList();
            return new ViewPage<>(result, count, maxPage, pageNum);
        } finally {
            em.close();
        }
    }
}
