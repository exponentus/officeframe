package staff.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.converter.LocalizedValConverter;
import com.exponentus.extconnect.IExtRole;
import com.exponentus.localization.constants.LanguageCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import staff.init.AppConst;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@JsonRootName("role")
@Entity
@Table(name = "staff__roles", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "SELECT m FROM staff.model.Role AS m ORDER BY m.regDate"),
        @NamedQuery(name = "Role.firedRole", query = "SELECT m FROM staff.model.Role AS m WHERE m.name='fired'")
})
public class Role extends SimpleReferenceEntity implements IExtRole {

    public static final String FIRED_ROLE_NAME = "fired";

    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees;

    @Convert(converter = LocalizedValConverter.class)
    @Column(name = "localized_descr", columnDefinition = "jsonb")
    private Map<LanguageCode, String> localizedDescr;

    @JsonIgnore
    public List<Employee> getEmployees() {
        return employees;
    }

    public Map<LanguageCode, String> getLocalizedDescr() {
        return localizedDescr;
    }

    public void setLocalizedDescr(Map<LanguageCode, String> localizedDescr) {
        this.localizedDescr = localizedDescr;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "roles/" + getIdentifier();
    }

    public String toString() {
        return name;
    }
}
