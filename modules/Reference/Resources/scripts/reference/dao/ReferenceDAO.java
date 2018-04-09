package reference.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Kayra 10-01-2016
 * <p>
 * Common superclass for all entities of the Reference module. To more
 * fine tuning reload it
 */
public abstract class ReferenceDAO<T extends IAppEntity<UUID>, K> extends DAO<T, K> {

    public ReferenceDAO(Class<T> entityClass, _Session session) throws DAOException {
        super(entityClass, session);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAllCategories() {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery("SELECT e.category FROM " + getEntityClass().getName()
                    + " AS e WHERE e.category != NULL AND e.category != '' GROUP BY e.category");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public T getFromMap(Map data, String entityTypeName, boolean isRequired) throws DAOException {
        Map<String, String> activityTypeMap = (Map<String, String>) data.get(entityTypeName);
        try {
            T entity = findByName(activityTypeMap.get("name"));
            return entity;
        }catch (NoResultException e){
            if (isRequired){
                throw e;
            }else{
                return null;
            }
        }
    }

    public T findByName(String name) throws DAOException {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
            Root<T> c = cq.from(getEntityClass());
            cq.select(c);
            Predicate condition = cb.equal(cb.lower(c.get("name")), name.toLowerCase());
            cq.where(condition);
            TypedQuery<T> typedQuery = em.createQuery(cq);
            return typedQuery.getSingleResult();
        } catch (NonUniqueResultException e) {
            throw new DAOException(e);
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<T> findAllByCategory(String categoryName) {
        return findAllByCategory(categoryName, 0, 0).getResult();
    }

    public T findByNameAndCategory(String category, String name) throws DAOException {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
            Root<T> c = cq.from(getEntityClass());
            cq.select(c);
            Predicate condition1 = cb.equal(cb.lower(c.get("category")), category.toLowerCase());
            Predicate condition2 = cb.equal(cb.lower(c.get("name")), name.toLowerCase());
            Predicate condition = cb.and(condition1, condition2);
            cq.where(condition);
            TypedQuery<T> typedQuery = em.createQuery(cq);
            return typedQuery.getSingleResult();
        } catch (NonUniqueResultException e) {
            throw new DAOException(e);
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

    public ViewPage<T> findAllByCategory(String categoryName, int pageNum, int pageSize) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<T> c = cq.from(getEntityClass());
            cq.select(c);
            countCq.select(cb.count(c));
            if (!categoryName.isEmpty()) {
                Predicate condition = cb.like(cb.lower(c.get("category")), categoryName);
                cq.where(condition);
                countCq.where(condition);
            }
            TypedQuery<T> typedQuery = em.createQuery(cq);
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
            List<T> result = typedQuery.getResultList();
            return new ViewPage<>(result, count, maxPage, pageNum);
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
                Predicate condition = cb.like(cb.lower(c.get("name")), "%" + keyword.toLowerCase() + "%");
                cq.where(condition);
                countCq.where(condition);
            }
            TypedQuery<T> typedQuery = em.createQuery(cq);
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
            List<T> result = typedQuery.getResultList();
            return new ViewPage<>(result, count, maxPage, pageNum);
        } finally {
            em.close();
        }
    }

    @Override
    public T add(T entity) throws SecureException, DAOException {
        T e = super.add(entity);
        resetCache();
        return e;
    }

    @Override
    public T update(T entity) throws SecureException, DAOException {
        T e = super.update(entity);
        resetCache();
        return e;
    }

    @Override
    public void delete(T entity) throws SecureException, DAOException {
        super.delete(entity);
        resetCache();
    }

}
