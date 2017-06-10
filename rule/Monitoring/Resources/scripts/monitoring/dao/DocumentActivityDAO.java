package monitoring.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.SimpleDAO;
import com.exponentus.extconnect.IMonitoringDAO;
import com.exponentus.runtimeobj.IAppEntity;
import com.exponentus.user.IUser;

import administrator.model.User;
import monitoring.model.DocumentActivity;
import monitoring.model.embedded.Event;

public class DocumentActivityDAO extends SimpleDAO<DocumentActivity> implements IMonitoringDAO {

	public DocumentActivityDAO() {
		super(DocumentActivity.class);
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
	public void postLogin(IUser<Long> user) throws DAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postLogout(IUser<Long> user) throws DAOException {
		// TODO Auto-generated method stub

	}

}
