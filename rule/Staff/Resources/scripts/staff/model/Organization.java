package staff.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.annotations.Customizer;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import reference.model.OrgCategory;
import staff.model.customizer.OrganizationCustomizer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "orgs", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "bin" }))
@Customizer(OrganizationCustomizer.class)
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
	@JoinTable(name = "orgs_labels", joinColumns = @JoinColumn(name = "org_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
	private List<OrganizationLabel> labels;
	
	@FTSearchable
	@Column(length = 12)
	private String bin = "";
	
	private int rank = 999;
	
	@JsonIgnore
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
	
	@JsonIgnore
	public List<OrganizationLabel> getLabels() {
		return labels;
	}
	
	public void setLabels(List<OrganizationLabel> labels) {
		this.labels = labels;
	}
	
	public List<String> getOrgLabelIds() {
		return Optional.of(labels).orElse(new ArrayList<>()).stream().map(it -> it.getIdentifier())
				.collect(Collectors.toList());
	}
	
	public int getRank() {
		return rank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public String getOrgCategoryId() {
		return orgCategory != null ? orgCategory.getIdentifier() : "";
	}
	
	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<bin>" + bin + "</bin>");
		chunk.append("<orgcategory id=\"" + orgCategory.getId() + "\">" + orgCategory.getLocalizedName(ses.getLang())
				+ "</orgcategory>");
		chunk.append("<labels>");
		for (OrganizationLabel l : labels) {
			chunk.append("<entry id=\"" + l.getId() + "\">" + l.getLocalizedName(ses.getLang()) + "</entry>");
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
