package staff.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("role")
@Entity
@Table(name = "staff_roles", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "Role.findAll", query = "SELECT m FROM Role AS m ORDER BY m.regDate")
public class Role extends SimpleReferenceEntity {

	@ManyToMany(mappedBy = "roles")
	private List<Employee> employees;

	@JsonIgnore
	public List<Employee> getEmployees() {
		return employees;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		return chunk.toString();
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		return "<name>" + getLocName(ses.getLang()) + "</name>";
	}
}
