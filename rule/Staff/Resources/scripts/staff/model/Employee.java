package staff.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.eclipse.persistence.config.CacheIsolationType;

import com.exponentus.common.model.Attachment;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.embedded.Avatar;
import com.exponentus.dataengine.system.IEmployee;
import com.exponentus.scripting._Session;
import com.exponentus.user.UndefinedUser;
import com.exponentus.util.StringUtil;
import com.exponentus.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import administrator.model.User;
import reference.model.Position;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "organization_id" }) )
@NamedQuery(name = "Employee.findAll", query = "SELECT m FROM Employee AS m ORDER BY m.regDate")
@Cache(isolation = CacheIsolationType.ISOLATED)
public class Employee extends SimpleReferenceEntity implements IEmployee {

	@OneToOne(cascade = { CascadeType.MERGE }, optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

	@Column(name = "birth_date")
	private Date birthDate;

	private String iin = "";

	@NotNull
	@ManyToOne(optional = true)
	@JoinColumn(nullable = false)
	private Organization organization;

	@NotNull
	@ManyToOne(optional = true)
	@JoinColumn(nullable = false)
	private Department department;

	@ManyToOne(optional = true)
	@JoinColumn(nullable = false)
	private Position position;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "employee_role")
	private List<Role> roles;

	@JsonIgnore
	@Embedded
	private Avatar avatar;

	@JsonIgnore
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@JsonIgnore
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@JsonIgnore
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
			roles = new ArrayList<Role>();
		}
		roles.add(r);
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
		List<Attachment> atts = new ArrayList<Attachment>();
		Attachment a = new Attachment();
		a.setRealFileName(StringUtil.getRandomText());
		a.setFieldName("avatar");
		atts.add(a);
		return atts;
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<name>" + getName() + "</name>");
		chunk.append("<login>" + getLogin() + "</login>");
		return chunk.toString();
	}

	@Override
	public List<String> getAllRoles() {
		List<String> list = new ArrayList<String>();
		if (roles == null) {
			return list;
		}

		for (Role r : roles) {
			list.add(r.getName());
		}
		return list;
	}

	String getOrganizationId() {
		return organization.getIdentifier();
	}

	String getDepartmentId() {
		return department.getIdentifier();
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<regdate>" + Util.convertDataTimeToString(regDate) + "</regdate>");
		chunk.append("<name>" + getName() + "</name>");
		chunk.append("<iin>" + iin + "</iin>");
		if (user != null) {
			chunk.append("<login>" + user.getLogin() + "</login>");
		}

		chunk.append("<birthdate>" + Util.convertDateToStringSilently(birthDate) + "</birthdate>");

		if (organization != null) {
			chunk.append("<organization id=\"" + organization.getId() + "\">" + organization.getLocalizedName(ses.getLang()) + "</organization>");
		}
		if (department != null) {
			chunk.append("<department id=\"" + department.getId() + "\">" + department.getLocalizedName(ses.getLang()) + "</department>");
		}

		if (position != null) {
			chunk.append("<position id=\"" + position.getId() + "\">" + position.getLocalizedName(ses.getLang()) + "</position>");
		}

		chunk.append("<roles>");
		if (roles != null) {
			for (Role l : roles) {
				chunk.append("<entry id=\"" + l.getId() + "\">" + l.getLocalizedName(ses.getLang()) + "</entry>");
			}
		}
		chunk.append("</roles>");

		return chunk.toString();
	}

}
