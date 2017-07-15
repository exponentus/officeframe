package monitoring.dao;

import administrator.model.User;
import com.exponentus.common.dao.SimpleDAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.extconnect.IMonitoringDAO;
import com.exponentus.log.Lg;
import com.exponentus.runtimeobj.IAppEntity;
import com.exponentus.user.IUser;
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

	public UserActivityDAO() {
		super(UserActivity.class);
		try {
			ip2c = new IP2Country(Environment.getKernelDir() + EnvConst.RESOURCES_DIR + File.separator + "ip-to-country.bin", IP2Country.MEMORY_CACHE);

		} catch (IOException e) {
			Lg.exception(e);
		}
	}

	@Override
	public void postEvent(IUser<Long> user, IAppEntity<UUID> entity, String descr) throws DAOException {

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

	public List<DocumentActivity> findAll(int firstRec, int pageSize) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			TypedQuery<DocumentActivity> q = em.createNamedQuery("UserActivity.findAll", DocumentActivity.class);
			q.setFirstResult(firstRec);
			q.setMaxResults(pageSize);
			return q.getResultList();
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
	public void postLogin(IUser<Long> user, String ip) throws DAOException {

		UserActivity ua = new UserActivity();
		ua.setEventTime(new Date());
		ua.setType(ActivityType.LOGIN);
		ua.setActUser(user.getId());
		ua.setIp(ip);

		if (!ip.equals("127.0.0.1") && !ip.equals("0:0:0:0:0:0:0:1")) {
			try {

				Country c = ip2c.getCountry(ip);
				ua.setCountry(c.getName());
			} catch (IOException e) {
				Lg.exception(e);
			}
		}
		add(ua);

	}

	@Override
	public void postLogout(IUser<Long> user) throws DAOException {
		UserActivity ua = new UserActivity();
		ua.setEventTime(new Date());
		ua.setType(ActivityType.LOGOUT);
		ua.setActUser(user.getId());
	//	add(ua);

	}

}
