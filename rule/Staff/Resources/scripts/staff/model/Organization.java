package staff.model;

import com.exponentus.common.model.SimpleHierarchicalReferenceEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.model.OrgCategory;

import javax.persistence.*;
import java.util.List;

@JsonRootName("organization")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "orgs", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "bin"}))
@NamedQuery(name = "Organization.findAll", query = "SELECT m FROM Organization AS m ORDER BY m.regDate")
public class Organization extends SimpleHierarchicalReferenceEntity {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private OrgCategory orgCategory;

    @OneToMany(mappedBy = "organization")
    private List<Department> departments;

    @OneToMany(mappedBy = "organization")
    private List<Employee> employers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "orgs_labels",
            joinColumns = @JoinColumn(name = "org_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
    private List<OrganizationLabel> labels;

    @FTSearchable
    @Column(length = 12)
    private String bin = "";

    private int rank = 999;

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

    @Override
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append(super.getFullXMLChunk(ses));
        chunk.append("<bin>" + bin + "</bin>");
        chunk.append("<orgcategory id=\"" + orgCategory.getId() + "\">" + orgCategory.getLocName(ses.getLang())
                + "</orgcategory>");
        chunk.append("<labels>");
        for (OrganizationLabel l : labels) {
            chunk.append("<entry id=\"" + l.getId() + "\">" + l.getLocName(ses.getLang()) + "</entry>");
        }
        chunk.append("</labels>");
        return chunk.toString();
    }

    @Override
    public String getShortXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append("<name>" + getName() + "</name>");
        chunk.append("<bin>" + bin + "</bin>");
        chunk.append("<labels>" + labels + "</labels>");
        return chunk.toString();
    }
}
