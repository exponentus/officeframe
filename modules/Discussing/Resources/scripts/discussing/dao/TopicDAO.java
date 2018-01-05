package discussing.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting._Session;
import discussing.dao.filter.TopicFilter;
import discussing.model.Topic;
import discussing.model.constants.TopicStatusType;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

public class TopicDAO extends DAO<Topic, UUID> {

    public TopicDAO(_Session session) throws DAOException {
        super(Topic.class, session);
    }

    public ViewPage<Topic> findViewPage(TopicFilter filter, SortParams sortParams, int pageNum, int pageSize) {
        if (filter == null) {
            throw new IllegalArgumentException("filter is null");
        }

        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<Topic> cq = cb.createQuery(Topic.class);
            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<Topic> root = cq.from(Topic.class);

            Predicate condition = null;

            if (!user.isSuperUser()) {
                condition = cb.and(root.get("readers").in(user.getId()));
            }

            if (filter.getAuthor() != null) {
                if (condition == null) {
                    condition = cb.equal(root.get("author"), filter.getAuthor());
                } else {
                    condition = cb.and(cb.equal(root.get("author"), filter.getAuthor()), condition);
                }
            }

            if (filter.getStatus() != TopicStatusType.UNKNOWN) {
                if (condition == null) {
                    condition = cb.equal(root.get("status"), filter.getStatus());
                } else {
                    condition = cb.and(cb.equal(root.get("status"), filter.getStatus()), condition);
                }
            }

            if (filter.getTags() != null) {
                if (condition == null) {
                    condition = cb.and(root.get("tags").in(filter.getTags()));
                } else {
                    condition = cb.and(root.get("tags").in(filter.getTags()), condition);
                }
            }

            cq.select(root).distinct(true).orderBy(collectSortOrder(cb, root, sortParams));
            countCq.select(cb.countDistinct(root));

            if (condition != null) {
                cq.where(condition);
                countCq.where(condition);
            }

            TypedQuery<Topic> typedQuery = em.createQuery(cq);
            Query query = em.createQuery(countCq);

            //TODO to test
            typedQuery.setHint(QueryHints.READ_ONLY, HintValues.TRUE);
            query.setHint(QueryHints.READ_ONLY, HintValues.TRUE);

            long count = (long) query.getSingleResult();
            int maxPage = pageable(typedQuery, count, pageNum, pageSize);

            List<Topic> result = typedQuery.getResultList();

            return new ViewPage<>(result, count, maxPage, pageNum);
        } finally {
            em.close();
        }
    }
}
