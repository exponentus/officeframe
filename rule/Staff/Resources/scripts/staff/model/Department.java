package staff.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.exponentus.common.model.HierarchicalEntity;
import com.exponentus.common.model.SimpleHierarchicalReferenceEntity;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import reference.model.DepartmentType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "departments", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "organization_id" }))
@NamedQuery(name = "Department.findAll", query = "SELECT m FROM Department AS m ORDER BY m.regDate")
public class Department extends SimpleHierarchicalReferenceEntity {

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private DepartmentType type;

	@NotNull
	@ManyToOne(optional = true)
	@JoinColumn(nullable = false)
	private Organization organization;

	@NotNull
	@ManyToOne(optional = true)
	@JoinColumn(nullable = false)
	private Department leadDepartment;

	@NotNull
	@ManyToOne(optional = true)
	@JoinColumn(nullable = false)
	private Employee boss;

	@OneToMany(mappedBy = "department")
	private List<Employee> employees;

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

	@JsonIgnore
	public List<Employee> getEmployees() {
		return employees;
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
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<regdate>" + TimeUtil.dateTimeToStringSilently(regDate) + "</regdate>");
		chunk.append("<name>" + getName() + "</name>");
		chunk.append("<type id=\"" + type.getId() + "\">" + type + "</type>");
		chunk.append("<rank>" + rank + "</rank>");
		chunk.append("<organization id=\"" + organization.getId() + "\">" + organization.getLocalizedName(ses.getLang())
				+ "</organization>");
		chunk.append("<localizednames>");
		try {
			LanguageDAO lDao = new LanguageDAO(ses);
			List<Language> list = lDao.findAllActivated();
			for (Language l : list) {
				chunk.append("<entry id=\"" + l.getCode() + "\">" + getLocalizedName(l.getCode()) + "</entry>");
			}
		} catch (DAOException e) {
			Server.logger.errorLogEntry(e);
		}
		chunk.append("</localizednames>");
		return chunk.toString();
	}
	
	@Override
	public HierarchicalEntity<UUID> getParentEntity(_Session ses) {
		if (organization != null) {
			return organization;
		} else {
			return leadDepartment;
		}
	}
}
