package staff.model;

import administrator.dao.CollationDAO;
import administrator.model.Collation;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.dao.OrgCategoryDAO;
import reference.model.OrgCategory;
import staff.init.ModuleConst;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@JsonRootName("organization")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = ModuleConst.CODE + "__orgs", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "bizID"}))
@NamedQuery(name = "Organization.findAll", query = "SELECT m FROM Organization AS m ORDER BY m.regDate")
public class Organization extends SimpleReferenceEntity {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private OrgCategory orgCategory;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    private List<Department> departments;

    @OneToMany(mappedBy = "organization")
    private List<Employee> employers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = ModuleConst.CODE + "__orgs_labels",
            joinColumns = @JoinColumn(name = "org_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
    private List<OrganizationLabel> labels;

    @FTSearchable
    @Column(length = 20, name = "biz_id")
    private String bizID = "";

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

    public String getBizID() {
        return bizID;
    }

    public void setBizID(String bizID) {
        this.bizID = bizID;
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
    public Organization compose(_Session ses, Map<String, ?> data) {
        super.compose(ses, data);
        try {
            Map<String, String> countryMap = (Map<String, String>) data.get("orgCategory");
            CollationDAO collationDAO = new CollationDAO(ses);
            Collation collation = collationDAO.findByExtKey(countryMap.get("id"));
            OrgCategoryDAO orgCategoryDAO = new OrgCategoryDAO(ses);
            OrgCategory orgCategory = orgCategoryDAO.findById(collation.getIntKey());
            this.orgCategory = orgCategory;
            bizID = (String) data.get("bizID");
            rank = (Integer) data.get("rank");
        } catch (Exception e) {
            Lg.exception(e);
            return null;
        }
        return this;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "organizations/" + getId();
    }
}
