package monitoring.dao;

import administrator.model.User;
import com.exponentus.common.dao.SimpleDAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import com.exponentus.util.TimeUtil;
import monitoring.dto.TimeChart;
import monitoring.model.DocumentActivity;
import monitoring.model.Statistic;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class StatisticDAO extends SimpleDAO<Statistic> {

	public StatisticDAO() {
		super(Statistic.class);
	}

	public StatisticDAO(_Session session) {
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
				Statistic prevStat = findByStatKeys(user, appCode, type, DateUtils.addDays(eventTime, -1), status, amount);
				if (prevStat == null) {
					Statistic s = findByStatKeys(user, appCode, type, eventTime, status);
					if (s != null) {
						if (amount != s.getAmount()) {
							s.setAmount(amount);
							update(s);
						}
					} else {
						add(ua);
					}
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

	public Statistic findByStatKeys(User user, String appCode, String type, Date eventTime, String status, long amount) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			return (Statistic) em
					.createQuery("SELECT s FROM Statistic s "
							+ "WHERE s.eventTime = :et AND s.actUser = :u AND s.appCode = :ac AND s.status=:s AND s.amount=:a")
					.setParameter("et", eventTime, TemporalType.DATE).setParameter("u", user.getId()).setParameter("ac", appCode)
					.setParameter("s", status).setParameter("a", amount).getSingleResult();
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

	public Long getCount() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("SELECT count(m) FROM Activity AS m");
			return (Long) q.getSingleResult();
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

	public TimeChart getStatusStat(String appCode, String type, IUser user, Date from, Date to, String status) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		TimeChart chart = new TimeChart();
		try {
			for (Date fromIter = from; !fromIter.after(to); fromIter = DateUtils.addDays(fromIter, 1)) {
				try {
					Statistic stat = (Statistic) em
							.createQuery(
									"SELECT s FROM Statistic s WHERE s.eventTime = :et AND s.appCode=:ac AND s.type = :t " +
											"AND s.actUser = :au AND s.status = :s")
							.setParameter("ac", appCode).setParameter("t", type).setParameter("au", user.getId())
							.setParameter("et", fromIter, TemporalType.DATE).setParameter("s", status).getSingleResult();
					chart.addValue(TimeUtil.dateToStringSilently(fromIter), stat.getAmount());
					chart.setType(type);
					chart.setStatus(status);
					chart.setStart(TimeUtil.dateToStringSilently(from));
					chart.setEnd(TimeUtil.dateToStringSilently(to));
				} catch (NonUniqueResultException e) {
					e.printStackTrace();
				} catch (NoResultException e) {

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return chart;
	}

}
