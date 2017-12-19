package staff.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.util.EmployeeConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Converters;
import reference.model.DepartmentType;
import staff.init.ModuleConst;
import staff.model.util.DepartmentConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JsonRootName("department")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "staff__departments", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "organization_id"}))
@Converters({@Converter(name = "dep_conv", converterClass = DepartmentConverter.class),
        @Converter(name = "emp_conv", converterClass = EmployeeConverter.class)})
@NamedQuery(name = "Department.findAll", query = "SELECT m FROM Department AS m ORDER BY m.regDate")
public class Department extends SimpleReferenceEntity {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private DepartmentType type;

    @NotNull
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private Organization organization;

    @Convert("dep_conv")
    @Basic(fetch = FetchType.LAZY, optional = true)
    private Department leadDepartment;

    @Convert("emp_conv")
    @Basic(fetch = FetchType.LAZY, optional = true)
    private Employee boss;

    private int rank = 999;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Department getLeadDepartment() {
        return leadDepartment;
    }

    public void setLeadDepartment(Department leadDepartment) {
        this.leadDepartment = leadDepartment;
    }

    public Employee getBoss() {
        return boss;
    }

    public void setBoss(Employee boss) {
        this.boss = boss;
    }

    public DepartmentType getType() {
        return type;
    }

    public void setType(DepartmentType type) {
        this.type = type;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "departments/" + getId();
    }
}
