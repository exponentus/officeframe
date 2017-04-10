package staff.dao;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import reference.model.OrgCategory;
import staff.dao.filter.OrganizationFilter;
import staff.model.Organization;
import staff.model.OrganizationLabel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class OrganizationDAO extends DAO<Organization, UUID> {

    public OrganizationDAO(_Session session) throws DAOException {
        super(Organization.class, session);
    }

    public ViewPage<Organization> findAll(OrganizationFilter filter, SortParams sortParams, int pageNum, int pageSize) {
        if (filter == null) {
            throw new IllegalArgumentException("filter is null");
        }

        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<Organization> cq = cb.createQuery(getEntityClass());
            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<Organization> root = cq.from(getEntityClass());

            Predicate condition = null;

            if (filter.getOrgCategory() != null) {
                condition = cb.and(cb.equal(root.get("orgCategory"), filter.getOrgCategory()));
            }

            if (filter.getLabels() != null && !filter.getLabels().isEmpty()) {
                if (condition != null) {
                    condition = cb.and(root.get("labels").in(filter.getLabels()), condition);
                } else {
                    condition = cb.and(root.get("labels").in(filter.getLabels()));
                }
            }

            if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
                if (condition != null) {
                    condition = cb.and(cb.like(cb.lower(root.<String>get("name")), "%" + filter.getKeyword().toLowerCase() + "%"), condition);
                } else {
                    condition = cb.like(cb.lower(root.<String>get("name")), "%" + filter.getKeyword().toLowerCase() + "%");
                }
            }

            cq.select(root);
            countCq.select(cb.count(root));

            if (condition != null) {
                cq.where(condition);
                countCq.where(condition);
            }

            cq.orderBy(collectSortOrder(cb, root, sortParams));

            TypedQuery<Organization> typedQuery = em.createQuery(cq);
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
            List<Organization> result = typedQuery.getResultList();
            return new ViewPage<>(result, count, maxPage, pageNum);
        } finally {
            em.close();
        }
    }

    public List<Organization> findPrimaryOrg() {
        try {
            ViewPage<Organization> result = findAllByLabel("primary", 1, 10);
            if (result != null && result.getCount() > 0) {
                return result.getResult();
            }
            return null;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public ViewPage<Organization> findAllByLabel(String labelName, int pageNum, int pageSize) {
        List<OrganizationLabel> val = new ArrayList<>();
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            OrganizationLabelDAO olDAO = new OrganizationLabelDAO(ses);
            OrganizationLabel l = olDAO.findByName(labelName);
            if (l != null) {
                val.add(l);
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<Organization> cq = cb.createQuery(getEntityClass());
                CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
                Root<Organization> c = cq.from(getEntityClass());
                cq.select(c);
                countCq.select(cb.count(c));
                Predicate condition = c.get("labels").in(val);
                cq.where(condition);
                countCq.where(condition);
                TypedQuery<Organization> typedQuery = em.createQuery(cq);
                Query query = em.createQuery(countCq);
                long count = (long) query.getSingleResult();
                int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
                if (pageNum == 0) {
                    pageNum = maxPage;
                }
                int firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
                typedQuery.setFirstResult(firstRec);
                typedQuery.setMaxResults(pageSize);
                List<Organization> result = typedQuery.getResultList();
                return new ViewPage<>(result, count, maxPage, pageNum);

            } else {
                return null;
            }
        } catch (DAOException e) {
            Server.logger.errorLogEntry(e);
            return null;
        } finally {
            em.close();
        }
    }

    public ViewPage<Organization> findAllByOrgCategory(List<OrgCategory> name, int pageNum, int pageSize) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<Organization> cq = cb.createQuery(getEntityClass());
            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<Organization> c = cq.from(getEntityClass());
            cq.select(c);
            countCq.select(cb.count(c));
            List<Predicate> predList = new LinkedList<>();
            for (OrgCategory cat : name) {
                predList.add(cb.or(cb.equal(c.get("orgCategory"), cat)));
            }
            Predicate[] predArray = new Predicate[predList.size()];
            predList.toArray(predArray);
            cq.where(predArray);
            countCq.where(predArray);
            TypedQuery<Organization> typedQuery = em.createQuery(cq);
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
            List<Organization> result = typedQuery.getResultList();
            return new ViewPage<>(result, count, maxPage, pageNum);
        } finally {
            em.close();
        }
    }

    public ViewPage<Organization> findAllByKeyword(String keyword, int pageNum, int pageSize) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<Organization> cq = cb.createQuery(getEntityClass());
            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<Organization> c = cq.from(getEntityClass());
            cq.select(c);
            countCq.select(cb.count(c));
            if (!keyword.isEmpty()) {
                Predicate condition = cb.like(cb.lower(c.<String>get("name")), "%" + keyword.toLowerCase() + "%");
                cq.where(condition);
                countCq.where(condition);
            }
            TypedQuery<Organization> typedQuery = em.createQuery(cq);
            Query query = em.createQuery(countCq);
            long count = (long) query.getSingleResult();
            int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
            if (pageNum == 0) {
                pageNum = maxPage;
            }
            int firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
            typedQuery.setFirstResult(firstRec);
            typedQuery.setMaxResults(pageSize);
            List<Organization> result = typedQuery.getResultList();
            return new ViewPage<>(result, count, maxPage, pageNum);
        } finally {
            em.close();
        }
    }
}
