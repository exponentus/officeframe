package staff.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import kz.flabs.util.Util;
import kz.lof.common.model.SimpleEntity;
import kz.lof.scripting._Session;
import reference.model.DepartmentType;

@Entity
@Table(name = "departments")
@NamedQuery(name = "Department.findAll", query = "SELECT m FROM Department AS m ORDER BY m.regDate")
public class Department extends SimpleEntity {

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private DepartmentType type;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private Organization organization;

	@OneToMany(mappedBy = "department")
	private List<Employee> employees;

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
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

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<regdate>" + Util.simpleDateFormat.format(regDate) + "</regdate>");
		chunk.append("<name>" + getName() + "</name>");
		chunk.append("<type>" + getType() + "</type>");
		chunk.append("<organization id=\"" + organization.getId() + "\">" + organization.getLocalizedName(ses.getLang()) + "</organization>");
		chunk.append("<localizednames>");
		LanguageDAO lDao = new LanguageDAO(ses);
		List<Language> list = lDao.findAll();
		for (Language l : list) {
			chunk.append("<entry id=\"" + l.getCode() + "\">" + getLocalizedName(l.getCode()) + "</entry>");
		}
		chunk.append("</localizednames>");
		return chunk.toString();
	}
}
