package reference.model;

import administrator.dao.CollationDAO;
import administrator.model.Collation;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpa.util.NamingCustomizer;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.*;
import org.eclipse.persistence.annotations.Customizer;
import reference.dao.LocalityDAO;
import reference.init.ModuleConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Customizer(NamingCustomizer.class)
@Table(name = ModuleConst.CODE + "__streets", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "locality_id"}))
@NamedQuery(name = "Street.findAll", query = "SELECT m FROM Street AS m ORDER BY m.regDate")
public class Street extends SimpleReferenceEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Locality locality;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CityDistrict cityDistrict;

    @Column(name = "street_id")
    private int streetId;

    @JsonIgnore
    @ElementCollection
    private Set<String> altName = new HashSet<>();

    public Set<String> getAltName() {
        return altName;
    }

    public void setAltName(Set<String> altName) {
        this.altName = altName;
    }

    public int getStreetId() {
        return streetId;
    }

    public void setStreetId(int streetId) {
        this.streetId = streetId;
    }

    public Locality getLocality() {
        return locality;
    }

    @JsonProperty
    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public CityDistrict getCityDistrict() {
        return cityDistrict;
    }

    public void setCityDistrict(CityDistrict cityDistrict) {
        this.cityDistrict = cityDistrict;
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
    public Street compose(_Session ses, Map<String, ?> data) {
        super.compose(ses, data);
        try {
            Map<String, String> localityMap = (Map<String, String>) data.get("locality");
            CollationDAO collationDAO = new CollationDAO(ses);
            Collation collation = collationDAO.findByExtKey(localityMap.get("id"));
            LocalityDAO localityDAO = new LocalityDAO(ses);
            Locality locality = localityDAO.findById(collation.getIntKey());
            this.locality = locality;

            streetId = (Integer) data.get("streetId");
        } catch (Exception e) {
            Lg.exception(e);
            return null;
        }
        return this;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "streets/" + getId();
    }
}
