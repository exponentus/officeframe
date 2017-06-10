package staff.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.DAO;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.exception.SecureException;
import com.exponentus.extconnect.IExtRole;
import com.exponentus.extconnect.IExtUser;
import com.exponentus.extconnect.IOfficeFrameDataProvider;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;

import staff.dao.filter.EmployeeFilter;
import staff.model.Employee;

public class EmployeeDAO extends DAO<Employee, UUID> implements IOfficeFrameDataProvider {
	private static ViewPage<Employee> allEmployee;
	private static Map<UUID, Employee> allEmployeeMap;

	public EmployeeDAO(_Session session) throws DAOException {
		super(Employee.class, session);
	}

	public ViewPage<Employee> findAll(boolean reloadCache) {
		if (allEmployee == null || reloadCache) {
			allEmployee = findAll(0, 0);
			allEmployeeMap = new HashMap<UUID, Employee>();
			for (Employee e : allEmployee.getResult()) {
				allEmployeeMap.put(e.getId(), e);
			}
		}
		return allEmployee;
	}

	@Override
	public Employee findById(UUID id) {
		if (allEmployeeMap == null) {
			findAll(true);
		}
		return allEmployeeMap.get(id);
	}

	public ViewPage<Employee> findAll(EmployeeFilter filter, SortParams sortParams, int pageNum, int pageSize) {
		if (filter == null) {
			throw new IllegalArgumentException("filter is null");
		}

		EntityManager em = getEntityManagerFactory().createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		try {
			CriteriaQuery<Employee> cq = cb.createQuery(getEntityClass());
			CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
			Root<Employee> root = cq.from(getEntityClass());

			Predicate condition = null;

			if (filter.getRoles() != null && !filter.getRoles().isEmpty()) {
				condition = cb.and(root.get("roles").in(filter.getRoles()));
			}

			if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
				if (condition != null) {
					condition = cb.and(cb.like(cb.lower(root.<String>get("name")), "%" + filter.getKeyword().toLowerCase() + "%"),
							condition);
				} else {
					condition = cb.like(cb.lower(root.<String>get("name")), "%" + filter.getKeyword().toLowerCase() + "%");
				}
			}

			cq.select(root);
			countCq.select(cb.count(root));

			if (condition != null) {
				cq.where(condition);
				countCq.where(condition);
			}

			cq.orderBy(collectSortOrder(cb, root, sortParams));

			TypedQuery<Employee> typedQuery = em.createQuery(cq);
			Query query = em.createQuery(countCq);
			long count = (long) query.getSingleResult();

			int maxPage = 1;
			if (pageNum != 0 || pageSize != 0) {
				maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
				if (pageNum == 0) {
					pageNum = maxPage;
				}
				int firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
				typedQuery.setFirstResult(firstRec);
				if (pageSize > 0) {
					typedQuery.setMaxResults(pageSize);
				}
			}
			List<Employee> result = typedQuery.getResultList();
			return new ViewPage<>(result, count, maxPage, pageNum);
		} finally {
			em.close();
		}
	}

	public Employee findByUser(IUser<Long> user) {
		return findByUserId(user.getId());
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

	public List<Employee> findAllByUserIds(List<Long> ids) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			String jpql = "SELECT m FROM Employee AS m WHERE m.user.id in :ids";
			TypedQuery<Employee> q = em.createQuery(jpql, Employee.class);
			q.setParameter("ids", ids);
			return q.getResultList();
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
			Predicate condition = cb.like(cb.lower(c.<String>get("name")), "%" + keyword.toLowerCase() + "%");
			cq.where(condition);
			countCq.where(condition);
			TypedQuery<Employee> typedQuery = em.createQuery(cq);
			Query query = em.createQuery(countCq);
			long count = (long) query.getSingleResult();
			int maxPage = 1;
			if (pageNum != 0 || pageSize != 0) {
				maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
				if (pageNum == 0) {
					pageNum = maxPage;
				}
				int firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
				typedQuery.setFirstResult(firstRec);
				typedQuery.setMaxResults(pageSize);
			}
			List<Employee> result = typedQuery.getResultList();
			return new ViewPage<>(result, count, maxPage, pageNum);
		} finally {
			em.close();
		}
	}

	@Override
	public IExtUser getEmployee(long id) {
		return findByUserId(id);
	}

	@Override
	public IExtUser getEmployee(UUID id) {
		return findById(id);
	}

	@Override
	public Employee add(Employee entity) throws SecureException, DAOException {
		Employee e = super.add(entity);
		allEmployee = null;
		allEmployeeMap = null;
		getEntityManagerFactory().getCache().evict(Employee.class);
		return e;
	}

	@Override
	public Employee update(Employee entity) throws SecureException, DAOException {
		Employee e = super.update(entity);
		allEmployee = null;
		allEmployeeMap = null;
		getEntityManagerFactory().getCache().evict(Employee.class);
		return e;
	}

	@Override
	public void delete(Employee entity) throws SecureException, DAOException {
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
				getEntityManagerFactory().getCache().evict(Employee.class);
			}
		} finally {
			em.close();
		}
		allEmployee = null;
		allEmployeeMap = null;
	}

	@Override
	public ViewPage<IExtRole> findAllRoles() {
		// TODO Auto-generated method stub
		return null;
	}

}
