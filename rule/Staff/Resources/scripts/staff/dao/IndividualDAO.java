package staff.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting._Session;
import staff.dao.filter.IndividualFilter;
import staff.model.Individual;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

public class IndividualDAO extends DAO<Individual, UUID> {

    public IndividualDAO(_Session session) throws DAOException {
        super(Individual.class, session);
    }

    public ViewPage<Individual> findAll(IndividualFilter filter, SortParams sortParams, int pageNum, int pageSize) {
        if (filter == null) {
            throw new IllegalArgumentException("filter is null");
        }

        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<Individual> cq = cb.createQuery(getEntityClass());
            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<Individual> root = cq.from(getEntityClass());

            Predicate condition = null;

            if (filter.getLabels() != null && !filter.getLabels().isEmpty()) {
                condition = cb.and(root.get("labels").in(filter.getLabels()));
            }

            if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
                if (condition == null) {
                    condition = cb.like(cb.lower(root.<String>get("name")), "%" + filter.getKeyword().toLowerCase() + "%");
                } else {
                    condition = cb.and(cb.like(cb.lower(root.<String>get("name")), "%" + filter.getKeyword().toLowerCase() + "%"), condition);
                }
            }

            cq.select(root);
            countCq.select(cb.count(root));

            if (condition != null) {
                cq.where(condition);
                countCq.where(condition);
            }

            cq.orderBy(collectSortOrder(cb, root, sortParams));

            TypedQuery<Individual> typedQuery = em.createQuery(cq);
            Query query = em.createQuery(countCq);
            long count = (long) query.getSingleResult();
            int maxPage = pageable(typedQuery, count, pageNum, pageSize);
            List<Individual> result = typedQuery.getResultList();
            return new ViewPage<>(result, count, maxPage, pageNum);
        } finally {
            em.close();
        }
    }
}
