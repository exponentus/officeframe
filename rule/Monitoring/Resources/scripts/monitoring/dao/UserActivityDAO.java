package monitoring.dao;

import administrator.model.User;
import com.exponentus.common.dao.SimpleDAO;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.extconnect.IMonitoringDAO;
import com.exponentus.log.Lg;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.user.IUser;
import monitoring.dao.filter.UserActivityFilter;
import monitoring.model.DocumentActivity;
import monitoring.model.UserActivity;
import monitoring.model.constants.ActivityType;
import monitoring.model.embedded.Event;
import net.firefang.ip2c.Country;
import net.firefang.ip2c.IP2Country;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserActivityDAO extends SimpleDAO<UserActivity> implements IMonitoringDAO {
    private static IP2Country ip2c;
    private _Session ses;

    public UserActivityDAO() {
        super(UserActivity.class);
        try {
            ip2c = new IP2Country(Environment.getKernelDir() + EnvConst.RESOURCES_DIR + File.separator + "ip-to-country.bin", IP2Country.MEMORY_CACHE);

        } catch (IOException e) {
            Lg.exception(e);
        }
    }

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

    public ViewPage getLastVisits() throws DAOException {
     /*   int colCount = 0;
        LanguageCode lang = ses.getLang();
       Table table = new Table();
        table.setName(Environment.vocabulary.getWord("site_traffic", lang));
        List<String> header = new ArrayList();
        header.add(Environment.vocabulary.getWord("user", lang));
        header.add(Environment.vocabulary.getWord("last_visit", lang));
        table.setHeader(header);

        List<Row> rows = new ArrayList();
        UserDAO userDAO = new UserDAO(ses);

        for (User user : userDAO.findAll()) {
            Row row = new Row();
            row.setName(user.getLogin());
            List<String> values = new ArrayList();
            values.add(Integer.toString(NumberUtil.getRandomNumber(10, 50)));
            row.setValues(values);
            rows.add(row);
        }

        table.setRows(rows);*/

        return new ViewPage();
    }

    public ViewPage findViewPage(UserActivityFilter filter, SortParams sortParams, int page, int pageSize) throws DAOException {
        return null;
    }


    @Override
    public void postEvent(IUser user, IAppEntity<UUID> entity, String descr) throws DAOException {

        DocumentActivity ua = new DocumentActivity();
        //ua.setType(ActivityType.COMPOSE);
        ua.setActEntityId(entity.getId());
        ua.setActEntityKind(entity.getEntityKind());
        //ua.setDetails(descr);
        //ua.setActUser(user.getId());
        ua.setEventTime(new Date());
        Event e = new Event();
        e.setTime(new Date());
        e.setAfterState(entity);
        ua.addEvent(e);
        add(ua);


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

    @Override
    public void postLogin(IUser user, String ip) throws DAOException {

        UserActivity ua = new UserActivity();
        ua.setEventTime(new Date());
        ua.setType(ActivityType.LOGIN);
        ua.setActUser((User) user);


        if (!ip.equals("127.0.0.1") && !ip.equals("0:0:0:0:0:0:0:1")) {
            try {
                ua.setIp(ip);
                Country c = ip2c.getCountry(ip);
                ua.setCountry(c.getName());
                add(ua);
            } catch (Exception e) {
                Lg.error("IP=" + ip);
                Lg.exception(e);
            }
        } else {
            ua.setIp("localhost");
            add(ua);
        }


    }

    @Override
    public void postLogout(IUser user) throws DAOException {
        UserActivity ua = new UserActivity();
        ua.setEventTime(new Date());
        ua.setType(ActivityType.LOGOUT);
        try {
            ua.setActUser((User) user);
        } catch (ClassCastException e) {

        }
        //	add(ua);

    }

}
