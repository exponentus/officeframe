package reference.dao;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting._Session;
import reference.model.Tag;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.UUID;

public class TagDAO extends ReferenceDAO<Tag, UUID> {

    public TagDAO(_Session session) throws DAOException {
        super(Tag.class, session);
    }

    public ViewPage<Tag> findAllByCategoryAndVisibility(SortParams sortParams, String categoryName, boolean withHidden, int pageNum, int pageSize) {
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

            List<Order> orderBy = collectSortOrder(cb, c, sortParams);
            if (orderBy != null) {
                cq.orderBy(orderBy);
            }

            cq.where(condition);
            countCq.where(condition);
            TypedQuery<Tag> typedQuery = em.createQuery(cq);
            Query query = em.createQuery(countCq);
            long count = (long) query.getSingleResult();
            int maxPage = pageable(typedQuery, count, pageNum, pageSize);
            List<Tag> result = typedQuery.getResultList();
            return new ViewPage<>(result, count, maxPage, pageNum);
        } finally {
            em.close();
        }
    }
}
