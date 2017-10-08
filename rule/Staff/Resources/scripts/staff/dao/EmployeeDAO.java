package staff.dao;

import com.exponentus.common.dao.DAO;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.extconnect.IExtRole;
import com.exponentus.extconnect.IExtUser;
import com.exponentus.extconnect.IOfficeFrameDataProvider;
import com.exponentus.log.Lg;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import staff.dao.filter.EmployeeFilter;
import staff.model.Employee;
import staff.model.Role;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;

public class EmployeeDAO extends DAO<Employee, UUID> implements IOfficeFrameDataProvider {
    private static ViewPage<Employee> allEmployee;
    private static Map<UUID, Employee> allEmployeeMap;
    private static Map<Long, Employee> allEmployeeId;

    public EmployeeDAO(_Session session) throws DAOException {
        super(Employee.class, session);
    }
//*****
    public void init(){
        findAll(true);
    }

    public ViewPage<Employee> findAll(boolean reloadCache) {
        if (allEmployee == null || reloadCache) {
            allEmployee = findAll(0, 0);
            allEmployeeMap = new HashMap<UUID, Employee>();
            allEmployeeId = new HashMap<Long, Employee>();
            for (Employee e : allEmployee.getResult()) {
                allEmployeeMap.put(e.getId(), e);
                allEmployeeId.put(e.getUserID(),e);
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
                condition = root.get("roles").in(filter.getRoles());
            }

            if (!filter.isWithFired()) {
                Role firedRole = em.createNamedQuery("Role.firedRole", Role.class).getSingleResult();
                Subquery<Employee> firedEmpSubquery = cq.subquery(Employee.class);
                Root<Employee> firedEmpRoot = firedEmpSubquery.from(Employee.class);
                firedEmpSubquery.select(firedEmpRoot.get("id")).where(cb.isMember(firedRole, firedEmpRoot.get("roles")));

                if (condition == null) {
                    condition = root.get("id").in(firedEmpSubquery).not();
                } else {
                    condition = cb.and(root.get("id").in(firedEmpSubquery).not(), condition);
                }
            }

            if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
                if (condition == null) {
                    condition = cb.like(cb.lower(root.<String>get("name")), "%" + filter.getKeyword().toLowerCase() + "%");
                } else {
                    condition = cb.and(cb.like(cb.lower(root.<String>get("name")), "%" + filter.getKeyword().toLowerCase() + "%"),
                            condition);
                }
            }

            cq.select(root).distinct(true);
            countCq.select(cb.countDistinct(root));

            if (condition != null) {
                cq.where(condition);
                countCq.where(condition);
            }

            cq.orderBy(collectSortOrder(cb, root, sortParams));

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

    public Employee findByUser(IUser user) {
        return findByUserId(user.getId());
    }

    public Employee findByUserId(long id) {
        return  allEmployeeId.get(id);
    }

    public ViewPage<Employee> findByRole(String roleName) throws DAOException {
        RoleDAO roleDAO = new RoleDAO(ses);
        Role role = roleDAO.findByName(roleName);
        if (role != null) {
            return findByRole(role);
        }else{
            return new ViewPage<>();
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
            Predicate condition = cb.like(cb.lower(c.<String>get("name")), "%" + keyword.toLowerCase() + "%");
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
    public IExtUser getEmployee(long id) {
        return findByUserId(id);
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
    public IExtRole createRole() {
        return new Role();
    }

    @Override
    public boolean saveRole(IExtRole role) {
        try {
            RoleDAO roleDAO = new RoleDAO(ses);
            Role existRole = roleDAO.findByName(role.getName());
           if (existRole == null){
               roleDAO.add((Role) role);
           }else{
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
