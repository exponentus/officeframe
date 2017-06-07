package monitoring.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.dataengine.jpa.SimpleDAO;
import com.exponentus.log.Lg;
import com.exponentus.util.TimeUtil;

import administrator.model.User;
import monitoring.model.DocumentActivity;
import monitoring.model.Statistic;

public class StatisticDAO extends SimpleDAO<Statistic> {

	public StatisticDAO() {
		super(Statistic.class);
	}

	public void postStat(User user, String appCode, String type, Date eventTime, String status, long amount) throws DAOException {
		if (amount > 0) {
			Statistic ua = new Statistic();
			ua.setActUser(user.getId());
			ua.setType(type);
			ua.setAmount(amount);
			ua.setAppCode(appCode);
			ua.setEventTime(eventTime);
			ua.setStatus(status);
			try {
				Statistic s = findByStatKeys(user, appCode, type, eventTime, status);
				if (s != null) {
					if (amount != s.getAmount()) {
						s.setAmount(amount);
						update(s);
					}
				} else {
					add(ua);
				}
			} catch (DAOException e) {
				if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
					Lg.warning("a data is already exists (" + user.getId() + "-" + appCode + "-" + TimeUtil.dateToStringSilently(eventTime)
							+ "-" + type + "-" + status + "), record was skipped");
				} else {
					Lg.exception(e);
				}
			}
		}

	}

	public Statistic findByStatKeys(User user, String appCode, String type, Date eventTime, String status) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			return (Statistic) em
					.createQuery(
							"SELECT s FROM Statistic s " + "WHERE s.eventTime = :et AND s.actUser = :u AND s.appCode = :ac AND s.status=:s")
					.setParameter("et", eventTime, TemporalType.DATE).setParameter("u", user.getId()).setParameter("ac", appCode)
					.setParameter("s", status).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}

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
			TypedQuery<DocumentActivity> q = em.createNamedQuery("Activity.findAll", DocumentActivity.class);
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

	public Statistic update(Statistic entity) throws DAOException {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.merge(entity);
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
