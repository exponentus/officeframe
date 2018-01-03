package staff.model;

import administrator.model.User;
import com.exponentus.common.model.Attachment;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.embedded.Avatar;
import com.exponentus.common.model.util.EmployeeConverter;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.exponentus.extconnect.IExtUser;
import com.exponentus.user.IUser;
import com.exponentus.user.UndefinedUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Converters;
import reference.model.Position;
import staff.init.ModuleConst;
import staff.model.util.DepartmentConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "staff__employees", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "organization_id"}),
        @UniqueConstraint(columnNames = {"user_id", "organization_id"})})
@Converters({@Converter(name = "dep_conv", converterClass = DepartmentConverter.class),
        @Converter(name = "emp_conv", converterClass = EmployeeConverter.class)})
@Cache(refreshOnlyIfNewer = true)
@JsonPropertyOrder({"kind", "name"})
public class Employee extends SimpleReferenceEntity implements IExtUser {

    @OneToOne(cascade = {CascadeType.MERGE}, optional = false, fetch = FetchType.EAGER)
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

    @JsonIgnore
    public IUser getUser() {
        return user;
    }

  /*  @Override
    public LanguageCode getDefaultLang() {
        return user.getDefaultLang();
    }*/

 /*   @Override
    public String getEmail() {
        return user.getEmail();
    }*/

   /* @Override
    public String getSlack() {
        return user.getSlack();
    }*/

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
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
            a.setExtension("png");
            a.setId(getId());
        } else {
            a.setRealFileName("no_avatar.png");
        }
        a.setFieldName("avatar");
        atts.add(a);
        return atts;
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

    public boolean isFired() {
        if (roles == null) {
            return false;
        }


        for (Role role : roles) {
            if (role.getName().equals(Role.FIRED_ROLE_NAME)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "employees/" + getId();
    }

    public String getAvatarURL() {
        if (avatar == null) {
            return null;
        }
        return ModuleConst.BASE_URL + "api/employees/" + getId() + "/avatar";
    }

    public String getAvatarURLSm() {
        if (avatar == null) {
            return null;
        }
        return ModuleConst.BASE_URL + "api/employees/" + getId() + "/avatar?_thumbnail";
    }
}
