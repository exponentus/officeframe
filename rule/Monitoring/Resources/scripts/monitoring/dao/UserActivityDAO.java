package monitoring.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.SimpleDAO;
import com.exponentus.extconnect.IMonitoringDAO;

import administrator.model.User;
import monitoring.model.UserActivity;
import monitoring.model.constants.ActivityType;

public class UserActivityDAO extends SimpleDAO<UserActivity> implements IMonitoringDAO {

	public UserActivityDAO() {
		super(UserActivity.class);
	}

	@Override
	public void postLogin(long id) throws DAOException {
		UserActivity ua = new UserActivity();
		ua.setType(ActivityType.LOGIN);
		ua.setActUser(id);
		ua.setEventTime(new Date());
		add(ua);

	}

	@Override
	public void postLogout(long id) throws DAOException {
		UserActivity ua = new UserActivity();
		ua.setType(ActivityType.LOGOUT);
		ua.setActUser(id);
		ua.setEventTime(new Date());
		add(ua);

	}

	public UserActivity findById(long id) {

		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<User> cq = cb.createQuery(User.class);
			Root<User> c = cq.from(User.class);
			cq.select(c);
			Predicate condition = c.get("id").in(id);
			cq.where(condition);
			Query query = em.createQuery(cq);
			UserActivity entity = (UserActivity) query.getSingleResult();
			return entity;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}

	}

	public List<UserActivity> findAll(int firstRec, int pageSize) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			TypedQuery<UserActivity> q = em.createNamedQuery("UserActivity.findAll", UserActivity.class);
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

	private UserActivity add(UserActivity entity) throws DAOException {
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

	public void delete(UserActivity entity) {
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
