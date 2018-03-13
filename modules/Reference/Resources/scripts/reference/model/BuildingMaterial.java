package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpa.util.NamingCustomizer;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.eclipse.persistence.annotations.Customizer;
import reference.init.ModuleConst;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Customizer(NamingCustomizer.class)
@Table(name = "ref__building_materials", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "BuildingMaterial.findAll", query = "SELECT m FROM BuildingMaterial AS m ORDER BY m.regDate")
public class BuildingMaterial extends SimpleReferenceEntity {

    @ElementCollection
    private Set<String> altName = new HashSet<>();

    public Set<String> getAltName() {
        return altName;
    }

    public void setAltName(Set<String> altName) {
        this.altName = altName;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "building-materials/" + getIdentifier();
    }
}
