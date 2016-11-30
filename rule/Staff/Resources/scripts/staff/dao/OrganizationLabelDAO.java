package staff.dao;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.scripting._Session;

import staff.model.OrganizationLabel;

public class OrganizationLabelDAO extends DAO<OrganizationLabel, UUID> {
	
	public OrganizationLabelDAO(_Session session) throws DAOException {
		super(OrganizationLabel.class, session);
	}
	
	public OrganizationLabel findByName(String name) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			String jpql = "SELECT m FROM OrganizationLabel AS m WHERE m.name = :name";
			TypedQuery<OrganizationLabel> q = em.createQuery(jpql, getEntityClass());
			q.setParameter("name", name);
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}
}
