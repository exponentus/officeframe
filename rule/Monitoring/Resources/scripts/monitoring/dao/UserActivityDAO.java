package monitoring.dao;

import administrator.model.User;
import com.exponentus.common.dao.SimpleDAO;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import monitoring.model.DocumentActivity;
import monitoring.model.UserActivity;
import net.firefang.ip2c.IP2Country;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserActivityDAO extends SimpleDAO<UserActivity> {
    private static IP2Country ip2c;
    private _Session ses;


    public UserActivityDAO(_Session ses) {
        super(UserActivity.class);
        this.ses = ses;
        try {
            ip2c = new IP2Country(Environment.getKernelDir() + EnvConst.RESOURCES_DIR + File.separator + "ip-to-country.bin", IP2Country.MEMORY_CACHE);
        } catch (IOException e) {
            Lg.exception(e);
        }
    }

    @Override
    public ViewPage<UserActivity> findAll(int pageNum, int pageSize) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<UserActivity> cq = cb.createQuery(UserActivity.class);
            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<UserActivity> c = cq.from(UserActivity.class);
            cq.select(c);
            countCq.select(cb.count(c));
            cq.orderBy(cb.desc(c.get("eventTime")));

            TypedQuery<UserActivity> typedQuery = em.createQuery(cq);
            Query query = em.createQuery(countCq);
            long count = (long) query.getSingleResult();
            int maxPage = pageable(typedQuery, count, pageNum, pageSize);

            List<UserActivity> result = typedQuery.getResultList();
            ViewPage<UserActivity> r = new ViewPage<UserActivity>(result, count, maxPage, pageNum);
            return r;
        } catch (Exception e) {
            Server.logger.exception(e);
        } finally {
            em.close();
        }
        return null;
    }

    public ViewPage<UserActivity> getLastVisits(int pageNum, int pageSize) throws DAOException {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        Query query = em.createQuery(
                "SELECT ua FROM UserActivity ua " +
                        " WHERE ua.eventTime = (SELECT MAX(e.eventTime) FROM UserActivity e WHERE ua.actUser = e.actUser)" +
                        " ORDER BY ua.eventTime desc");
        Query queryCount = em.createQuery(
                "SELECT COUNT(ua.actUser) FROM UserActivity ua GROUP BY ua.actUser ");

        long count = queryCount.getResultList().size();
        int maxPage = pageable(query, count, pageNum, pageSize);
        List<UserActivity> result = query.getResultList();
        return new ViewPage<UserActivity>(result, count, maxPage, pageNum);
    }



    public DocumentActivity findById(long id) {

        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> c = cq.from(User.class);
            cq.select(c);
            Predicate condition = c.get("id").in(id);
            cq.where(condition);
            Query query = em.createQuery(cq);
            DocumentActivity entity = (DocumentActivity) query.getSingleResult();
            return entity;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Long getCount() {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            Query q = em.createQuery("SELECT count(m) FROM UserActivity AS m");
            return (Long) q.getSingleResult();
        } finally {
            em.close();
        }
    }

    public DocumentActivity add(DocumentActivity entity) throws DAOException {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                em.persist(entity);
                t.commit();
                return entity;
            } catch (PersistenceException e) {
                throw new DAOException(e);
            } finally {
                if (t.isActive()) {
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
    }

    public void delete(DocumentActivity entity) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                entity = em.merge(entity);
                em.remove(entity);
                t.commit();
            } finally {
                if (t.isActive()) {
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
    }


}
