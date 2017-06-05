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

import administrator.model.User;
import monitoring.model.Activity;
import monitoring.model.Statistic;

public class StatisticDAO extends SimpleDAO<Statistic> {

	public StatisticDAO() {
		super(Statistic.class);
	}

	public void postStat(User user, String appCode, String type, Date eventTime, long amount) throws DAOException {
		if (amount > 0) {
			Statistic ua = new Statistic();
			ua.setActUser(user.getId());
			ua.setType(type);
			ua.setAmount(amount);
			ua.setAppCode(appCode);
			ua.setEventTime(eventTime);
			add(ua);
		}

	}

	public Activity findById(long id) {

		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<User> cq = cb.createQuery(User.class);
			Root<User> c = cq.from(User.class);
			cq.select(c);
			Predicate condition = c.get("id").in(id);
			cq.where(condition);
			Query query = em.createQuery(cq);
			Activity entity = (Activity) query.getSingleResult();
			return entity;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}

	}

	public List<Activity> findAll(int firstRec, int pageSize) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			TypedQuery<Activity> q = em.createNamedQuery("Activity.findAll", Activity.class);
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
			Query q = em.createQuery("SELECT count(m) FROM Activity AS m");
			return (Long) q.getSingleResult();
		} finally {
			em.close();
		}
	}

	public Statistic add(Statistic entity) throws DAOException {
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

	public void delete(Activity entity) {
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
