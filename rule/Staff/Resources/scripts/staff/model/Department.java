package staff.model;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.exponentus.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.model.DepartmentType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "departments")
@NamedQuery(name = "Department.findAll", query = "SELECT m FROM Department AS m ORDER BY m.regDate")
public class Department extends SimpleReferenceEntity {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private DepartmentType type;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Organization organization;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    @JsonIgnore
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

    String getOrganizationId() {
        return organization.getIdentifier();
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
