package staff.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.exponentus.common.model.HierarchicalEntity;
import com.exponentus.common.model.SimpleHierarchicalReferenceEntity;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import reference.model.DepartmentType;

@JsonRootName("department")
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

	//@NotNull
	@ManyToOne(optional = true)
	//@JoinColumn(nullable = false) // как добавить департамент, если еще нет ни одного
	private Department leadDepartment;

	//@NotNull
	@ManyToOne(optional = true)
	//@JoinColumn(nullable = false) // без Employee нельзя добавить Department, без Department нельзя добавить Employee
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
		chunk.append("<organization id=\"" + organization.getId() + "\">" + organization.getLocName(ses.getLang())
				+ "</organization>");
		if (leadDepartment != null) {
			chunk.append("<leadDepartment id=\"" + leadDepartment.getId() + "\">"
					+ leadDepartment.getLocName(ses.getLang()) + "</leadDepartment>");
		}
		chunk.append("<localizednames>");
		try {
			LanguageDAO lDao = new LanguageDAO(ses);
			List<Language> list = lDao.findAllActivated();
			for (Language l : list) {
				chunk.append("<entry id=\"" + l.getCode() + "\">" + getLocName(l.getCode()) + "</entry>");
			}
		} catch (DAOException e) {
			Server.logger.errorLogEntry(e);
		}
		chunk.append("</localizednames>");
		return chunk.toString();
	}

	@Override
	public HierarchicalEntity<UUID> getParentEntity(_Session ses) {
		if (leadDepartment != null) {
			return leadDepartment;
		} else {
			return organization;

		}
	}
}
