package staff.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.exceptions.DatabaseException;

import administrator.dao.UserDAO;
import kz.flabs.runtimeobj.RuntimeObjUtil;
import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.dataengine.system.IEmployee;
import com.exponentus.dataengine.system.IEmployeeDAO;
import com.exponentus.scripting._Session;
import staff.model.Employee;

public class EmployeeDAO extends DAO<Employee, UUID>implements IEmployeeDAO {

	public EmployeeDAO(_Session session) {
		super(Employee.class, session);
	}

	public Employee findByUserId(long id) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			String jpql = "SELECT m FROM Employee AS m WHERE m.user.id = :id";
			TypedQuery<Employee> q = em.createQuery(jpql, Employee.class);
			q.setParameter("id", id);
			List<Employee> res = q.getResultList();
			return res.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public ViewPage<Employee> findAllByName(String keyword, int pageNum, int pageSize) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<Employee> cq = cb.createQuery(getEntityClass());
			CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
			Root<Employee> c = cq.from(getEntityClass());
			cq.select(c);
			countCq.select(cb.count(c));
			Predicate condition = cb.like(cb.lower(c.<String> get("name")), "%" + keyword.toLowerCase() + "%");
			cq.where(condition);
			countCq.where(condition);
			TypedQuery<Employee> typedQuery = em.createQuery(cq);
			Query query = em.createQuery(countCq);
			long count = (long) query.getSingleResult();
			int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
			if (pageNum == 0) {
				pageNum = maxPage;
			}
			int firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
			typedQuery.setFirstResult(firstRec);
			typedQuery.setMaxResults(pageSize);
			List<Employee> result = typedQuery.getResultList();
			return new ViewPage<Employee>(result, count, maxPage, pageNum);
		} finally {
			em.close();
		}
	}

	@Override
	public IEmployee getEmployee(long id) {
		return findByUserId(id);
	}

	@Override
	public Employee add(Employee entity) throws DatabaseException {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				entity.setAuthor(user.getId());
				entity.setForm(entity.getDefaultFormName());
				UserDAO.normalizePwd(entity.getUser());
				em.persist(entity);
				t.commit();
				update(entity);
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

	@Override
	public Employee update(Employee entity) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				UserDAO.normalizePwd(entity.getUser());
				em.merge(entity);
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
