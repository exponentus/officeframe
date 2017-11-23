package staff.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.model.OrgCategory;
import staff.init.AppConst;

import javax.persistence.*;
import java.util.List;

@JsonRootName("organization")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "staff__orgs", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "bin"}))
@NamedQuery(name = "Organization.findAll", query = "SELECT m FROM Organization AS m ORDER BY m.regDate")
public class Organization extends SimpleReferenceEntity {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private OrgCategory orgCategory;

    @OneToMany(mappedBy = "organization")
    private List<Department> departments;

    @OneToMany(mappedBy = "organization")
    private List<Employee> employers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "staff__orgs_labels",
            joinColumns = @JoinColumn(name = "org_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
    private List<OrganizationLabel> labels;

    @FTSearchable
    @Column(length = 12)
    private String bin = "";

    private int rank = 999;

    @Column(name = "is_primary")
    private boolean isPrimary;

    public OrgCategory getOrgCategory() {
        return orgCategory;
    }

    public void setOrgCategory(OrgCategory orgCategory) {
        this.orgCategory = orgCategory;
    }

    @JsonIgnore
    public List<Department> getDepartments() {
        return departments;
    }

    @JsonIgnore
    public List<Employee> getEmployers() {
        return employers;
    }

    public void setEmployers(List<Employee> employers) {
        this.employers = employers;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public List<OrganizationLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<OrganizationLabel> labels) {
        this.labels = labels;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }


    @Override
    public String getURL() {
        return AppConst.BASE_URL + "organizations/" + getId();
    }
}
