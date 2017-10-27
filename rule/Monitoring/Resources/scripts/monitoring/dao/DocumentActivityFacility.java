package monitoring.dao;

import administrator.model.User;
import com.exponentus.common.dao.SimpleDAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.extconnect.IMonitoringFacility;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.user.IUser;
import monitoring.model.DocumentActivity;
import monitoring.model.embedded.Event;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.UUID;

public class DocumentActivityFacility extends SimpleDAO<DocumentActivity> implements IMonitoringFacility {

	public DocumentActivityFacility() {
		super(DocumentActivity.class);
	}


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
			Query q = em.createQuery("SELECT count(m) FROM DocumentActivity AS m");
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


	public void postLogin(IUser user, String ip) throws DAOException {
		// TODO Auto-generated method stub

	}


	public void postLogout(IUser user) throws DAOException {
		// TODO Auto-generated method stub

	}

}