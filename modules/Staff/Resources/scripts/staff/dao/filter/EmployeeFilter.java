package staff.dao.filter;

import administrator.model.User;
import com.exponentus.dataengine.IFilter;
import staff.init.ModuleConst;
import staff.model.Employee;
import staff.model.Role;

import javax.persistence.criteria.*;
import java.util.List;

public class EmployeeFilter implements IFilter<Employee> {

    private List<User> users;
    private List<Role> roles;
    private boolean withFired;
    private String keyword;

    public EmployeeFilter() {
    }

    public EmployeeFilter(List<Role> roles) {
        this.roles = roles;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isWithFired() {
        return withFired;
    }

    public void setWithFired(boolean withFired) {
        this.withFired = withFired;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Predicate collectPredicate(Root<Employee> root, CriteriaBuilder cb, Predicate condition) {
        if (roles != null && !roles.isEmpty()) {
            if (condition == null) {
                condition = root.get("roles").in(roles);
            } else {
                condition = cb.and(root.get("roles").in(roles), condition);
            }
        }

        if (users != null && !users.isEmpty()) {
            if (condition == null) {
                condition = root.get("user").in(users);
            } else {
                condition = cb.and(root.get("user").in(users), condition);
            }
        }

        if (!withFired) {
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            Subquery<Employee> firedEmpSubquery = cq.subquery(Employee.class);
            Root<Employee> firedEmpRoot = firedEmpSubquery.from(Employee.class);
            firedEmpSubquery.select(firedEmpRoot.get("id")).where(cb.isMember(ModuleConst.ROLE_FIRED, firedEmpRoot.get("roles").get("name")));

            if (condition == null) {
                condition = root.get("id").in(firedEmpSubquery).not();
            } else {
                condition = cb.and(root.get("id").in(firedEmpSubquery).not(), condition);
            }
        }

        if (keyword != null && !keyword.isEmpty()) {
            if (condition == null) {
                condition = cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
            } else {
                condition = cb.and(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                        condition);
            }
        }

        return condition;
    }
}
