package staff.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Converters;

import com.exponentus.common.model.Attachment;
import com.exponentus.common.model.HierarchicalEntity;
import com.exponentus.common.model.SimpleHierarchicalReferenceEntity;
import com.exponentus.common.model.embedded.Avatar;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.exponentus.dataengine.system.IEmployee;
import com.exponentus.scripting._Session;
import com.exponentus.user.UndefinedUser;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import administrator.model.User;
import reference.model.Position;
import staff.model.util.DepartmentConverter;
import staff.model.util.EmployeeConverter;

@JsonRootName("employee")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "staff__employees", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "organization_id" }))
@NamedQuery(name = "EmployeCacheTypee.findAll", query = "SELECT m FROM Employee AS m ORDER BY m.regDate")
@Converters({ @Converter(name = "dep_conv", converterClass = DepartmentConverter.class),
		@Converter(name = "emp_conv", converterClass = EmployeeConverter.class) })
@Cache(refreshOnlyIfNewer = true)
@JsonPropertyOrder({ "kind", "name" })
public class Employee extends SimpleHierarchicalReferenceEntity implements IEmployee {

	@OneToOne(cascade = { CascadeType.MERGE }, optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "birth_date")
	private Date birthDate;

	@FTSearchable
	private String iin = "";

	@NotNull
	@ManyToOne(optional = true)
	@JoinColumn(nullable = false)
	private Organization organization;

	@Convert("dep_conv")
	@Basic(fetch = FetchType.LAZY, optional = true)
	private Department department;

	@Convert("emp_conv")
	@Basic(fetch = FetchType.LAZY, optional = true)
	private Employee boss;

	@ManyToOne(optional = true)
	@JoinColumn(nullable = false)
	private Position position;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "staff__employee_role")
	private List<Role> roles;

	private int rank = 999;

	@JsonIgnore
	@Embedded
	private Avatar avatar;

	// @JsonIgnore
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	// @JsonIgnore
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Employee getBoss() {
		return boss;
	}

	public void setBoss(Employee boss) {
		this.boss = boss;
	}

	// @JsonIgnore
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getIin() {
		return iin;
	}

	public void setIin(String iin) {
		this.iin = iin;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public String getLogin() {
		if (user != null) {
			return user.getLogin();
		} else {
			return UndefinedUser.USER_NAME;
		}
	}

	@Override
	public Long getUserID() {
		if (user != null) {
			return user.getId();
		} else {
			return UndefinedUser.ID;
		}
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role r) {
		if (roles == null) {
			roles = new ArrayList<>();
		}
		roles.add(r);
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@JsonIgnore
	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	@Override
	public List<Attachment> getAttachments() {
		List<Attachment> atts = new ArrayList<>();
		Attachment a = new Attachment();
		if (avatar != null && !avatar.getRealFileName().isEmpty()) {
			a.setRealFileName(avatar.getRealFileName());
		} else {
			a.setRealFileName("no_avatar.png");
		}
		a.setFieldName("avatar");
		atts.add(a);
		return atts;
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<rank>" + rank + "</rank>");
		chunk.append("<name>" + getName() + "</name>");
		chunk.append("<login>" + getLogin() + "</login>");
		chunk.append("<roles>");
		if (roles != null) {
			for (Role l : roles) {
				chunk.append("<entry id=\"" + l.getId() + "\">" + l.getName() + "</entry>");
			}
		}
		chunk.append("</roles>");
		return chunk.toString();
	}

	@Override
	public List<String> getAllRoles() {
		List<String> list = new ArrayList<>();
		if (roles == null) {
			return list;
		}

		for (Role r : roles) {
			list.add(r.getName());
		}
		return list;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<regdate>" + TimeUtil.dateTimeToStringSilently(regDate) + "</regdate>");
		chunk.append("<name>" + getName() + "</name>");
		chunk.append("<iin>" + iin + "</iin>");
		chunk.append("<rank>" + rank + "</rank>");
		if (user != null) {
			chunk.append("<login>" + user.getLogin() + "</login>");
		}

		chunk.append("<birthdate>" + TimeUtil.dateTimeToStringSilently(birthDate) + "</birthdate>");

		if (organization != null) {
			chunk.append("<organization id=\"" + organization.getId() + "\">" + organization.getLocName(ses.getLang())
					+ "</organization>");
		}
		if (department != null) {
			chunk.append("<department id=\"" + department.getId() + "\">" + department.getLocName(ses.getLang())
					+ "</department>");
		}

		chunk.append("<position id=\"" + position.getId() + "\">" + position.getLocName(ses.getLang()) + "</position>");

		chunk.append("<roles>");
		if (roles != null) {
			for (Role l : roles) {
				chunk.append("<entry id=\"" + l.getId() + "\">" + l.getLocName(ses.getLang()) + "</entry>");
			}
		}
		chunk.append("</roles>");

		return chunk.toString();
	}

	@Override
	public HierarchicalEntity<UUID> getParentEntity(_Session ses) {
		if (department != null) {
			return department;
		} else {
			return organization;
		}
	}
}
