package staff.dao.filter;

import com.exponentus.runtimeobj.Filter;
import staff.model.Role;

import java.util.List;

public class EmployeeFilter extends Filter {

    private List<Role> roles;
    private String keyword;

    public EmployeeFilter() {
    }

    public EmployeeFilter(List<Role> roles) {
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
