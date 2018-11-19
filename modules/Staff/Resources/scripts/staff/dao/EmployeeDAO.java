package staff.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.modulebinding.IExtRole;
import com.exponentus.modulebinding.IExtUser;
import com.exponentus.modulebinding.IOfficeFrame;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import staff.model.Employee;
import staff.model.Role;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

public class EmployeeDAO extends DAO<Employee, UUID> implements IOfficeFrame {
    private static ViewPage<Employee> allEmployee;
    private static Map<UUID, Employee> allEmployeeMap;
    private static Map<Long, Employee> allEmployeeId;

    public EmployeeDAO(_Session session) throws DAOException {
        super(Employee.class, session);
    }


    public void init() {
        findAll(true);
    }

    public ViewPage<Employee> findAll(boolean reloadCache) {
        if (allEmployee == null || reloadCache) {
            allEmployee = findViewPage(0, 0);
            allEmployeeMap = new HashMap<UUID, Employee>();
            allEmployeeId = new HashMap<Long, Employee>();
            for (Employee e : allEmployee.getResult()) {
                allEmployeeMap.put(e.getId(), e);
                allEmployeeId.put(e.getUserID(), e);
            }
        }
        return allEmployee;
    }

    @Override
    public Employee findById(UUID id) {
        if (allEmployeeMap == null) {
            init();
        }
        return allEmployeeMap.get(id);
    }

    public String getUserName(IUser user) {
        Employee emp = findByUserId(user.getId());
        if (emp == null) {
            return user.getLogin();
        } else {
            return emp.getName();
        }
    }

    public Employee findByUser(IUser user) {
        return findByUserId(user.getId());
    }

    public Employee findByUserId(long id) {
        if (allEmployeeId == null) {
            init();
        }
        return allEmployeeId.get(id);
    }

    public String getEmployeeNameSilently(long id) {
        Employee employee = allEmployeeId.get(id);
        if (employee != null) {
            return employee.getName();
        }else{
            return "";
        }
    }

    public ViewPage<Employee> findByRole(String roleName) throws DAOException {
        RoleDAO roleDAO = new RoleDAO(ses);
        Role role = roleDAO.findByName(roleName);
        if (role != null) {
            return findByRole(role);
        } else {
            throw new DAOException(DAOExceptionType.NO_ROLE, roleName);
        }
    }


    public ViewPage<Employee> findByRole(Role role) throws DAOException {
        int pageNum = 1, pageSize = 100;
        EntityManager em = getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {
            CriteriaQuery<Employee> cq = cb.createQuery(getEntityClass());
            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<Employee> c = cq.from(getEntityClass());
            cq.select(c);
            countCq.select(cb.count(c));
            Predicate condition = cb.isMember(role, c.<Collection>get("roles"));
            cq.where(condition);
            countCq.where(condition);
            TypedQuery<Employee> typedQuery = em.createQuery(cq);
            Query query = em.createQuery(countCq);
            long count = (long) query.getSingleResult();
            int maxPage = pageable(typedQuery, count, pageNum, pageSize);
            List<Employee> result = typedQuery.getResultList();
            return new ViewPage<>(result, count, maxPage, pageNum);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        } catch (Exception e) {
            throw new DAOException(e);
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
            Predicate condition = cb.like(cb.lower(c.get("name")), "%" + keyword.toLowerCase() + "%");
            cq.where(condition);
            countCq.where(condition);
            TypedQuery<Employee> typedQuery = em.createQuery(cq);
            Query query = em.createQuery(countCq);
            long count = (long) query.getSingleResult();
            int maxPage = pageable(typedQuery, count, pageNum, pageSize);
            List<Employee> result = typedQuery.getResultList();
            return new ViewPage<>(result, count, maxPage, pageNum);
        } finally {
            em.close();
        }
    }

    @Override
    @Deprecated
    public IExtUser getEmployee(long id) {
        return findByUserId(id);
    }


    @Override
    public Employee add(Employee entity) throws SecureException, DAOException {
        Employee e = super.add(entity);
        allEmployee = null;
        allEmployeeMap = null;
        allEmployeeId = null;
        getEntityManagerFactory().getCache().evict(Employee.class);
        return e;
    }

    @Override
    public Employee update(Employee entity) throws SecureException, DAOException {
        Employee e = super.update(entity);
        allEmployee = null;
        allEmployeeMap = null;
        allEmployeeId = null;
        getEntityManagerFactory().getCache().evict(Employee.class);
        return e;
    }

    @Override
    public void delete(Employee entity) {
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
        allEmployeeId = null;
    }


    @Override
    public IExtRole createRole() {
        return new Role();
    }

    @Override
    public boolean saveRole(IExtRole role) {
        try {
            RoleDAO roleDAO = new RoleDAO(ses);
            Role existRole = roleDAO.findByName(role.getName());
            if (existRole == null) {
                roleDAO.add((Role) role);
            } else {
                existRole.setLocName(role.getLocName());
                existRole.setLocalizedDescr(role.getLocalizedDescr());
                roleDAO.update(existRole);
            }
            return true;
        } catch (DAOException | SecureException e) {
            Lg.exception(e);
            return false;
        }

    }
}
