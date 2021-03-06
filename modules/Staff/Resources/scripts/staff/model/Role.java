package staff.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.converter.LocalizedValConverter;
import com.exponentus.modulebinding.IExtRole;
import com.exponentus.localization.constants.LanguageCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import staff.init.ModuleConst;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "staff__roles", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "SELECT m FROM staff.model.Role AS m ORDER BY m.regDate"),
        @NamedQuery(name = "Role.firedRole", query = "SELECT m FROM staff.model.Role AS m WHERE m.name='fired'")
})
public class Role extends SimpleReferenceEntity implements IExtRole {

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
        return ModuleConst.BASE_URL + "roles/" + getId();
    }

    public String toString() {
        return name;
    }
}
