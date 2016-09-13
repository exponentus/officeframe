package monitoring.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.eclipse.persistence.exceptions.DatabaseException;

import com.exponentus.dataengine.IDatabase;
import com.exponentus.dataengine.system.IMonitoringDAO;
import com.exponentus.env.Environment;
import com.exponentus.scripting._Session;

import monitoring.model.UserActivity;
import monitoring.model.constants.ActivityType;

public class UserActivityDAO implements IMonitoringDAO {
	private EntityManagerFactory emf;

	public UserActivityDAO(_Session ses) {
		IDatabase db = Environment.adminApplication.getDataBase();
		emf = db.getEntityManagerFactory();
	}

	@Override
	public void postLogin(long id) {
		UserActivity ua = new UserActivity();
		ua.setType(ActivityType.LOGIN);
		ua.setActUser(id);
		ua.setEventTime(new Date());
		add(ua);

	}

	@Override
	public void postLogout(long id) {
		UserActivity ua = new UserActivity();
		ua.setType(ActivityType.LOGOUT);
		ua.setActUser(id);
		ua.setEventTime(new Date());
		add(ua);

	}

	private UserActivity add(UserActivity entity) throws DatabaseException {
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.persist(entity);
				t.commit();
				return entity;
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
