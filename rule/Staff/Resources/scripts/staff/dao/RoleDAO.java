package staff.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import staff.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class RoleDAO extends DAO<Role, UUID> {
	
	public RoleDAO(_Session session) throws DAOException {
		super(Role.class, session);
	}
	
	public Role findByName(String tagName) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<Role> cq = cb.createQuery(Role.class);
			Root<Role> c = cq.from(Role.class);
			cq.select(c);
			Predicate condition = cb.equal(c.get("name"), tagName);
			cq.where(condition);
			Query query = em.createQuery(cq);
			Role entity = (Role) query.getSingleResult();
			return entity;
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}
	
}
