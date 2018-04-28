package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpa.util.NamingCustomizer;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.eclipse.persistence.annotations.Customizer;
import reference.init.ModuleConst;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Customizer(NamingCustomizer.class)
@Table(name = ModuleConst.CODE + "__building_materials", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "BuildingMaterial.findAll", query = "SELECT m FROM BuildingMaterial AS m ORDER BY m.regDate")
public class BuildingMaterial extends SimpleReferenceEntity {

    @JsonIgnore
    @ElementCollection
    private Set<String> altName = new HashSet<>();

    public Set<String> getAltName() {
        return altName;
    }

    public void setAltName(Set<String> altName) {
        this.altName = altName;
    }

    @JsonGetter("altName")
    public String getAlternateName() {
        return String.join("\n", altName);
    }

    @JsonSetter("altName")
    public void setAlternateName(String name) {
        altName = Stream.of(Optional.of(name).orElse("").split("\n")).collect(Collectors.toSet());
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "building-materials/" + getId();
    }
}
