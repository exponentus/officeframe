package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonRootName("locality")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__localities", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type_id", "region_id"}))
public class Locality extends SimpleReferenceEntity {

    @JsonIgnore
    @OneToMany(mappedBy = "locality")
    @OrderBy("name ASC")
    private List<Street> streets;

    @JsonIgnore
    @OneToMany(mappedBy = "locality")
    @OrderBy("name ASC")
    private List<CityDistrict> districts;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private LocalityType type;

    // @JsonIgnore
    @NotNull
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private Region region;

    public LocalityType getType() {
        return type;
    }

    public void setType(LocalityType type) {
        this.type = type;
    }

    public List<CityDistrict> getDistricts() {
        return districts;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setStreets(List<Street> streets) {
        this.streets = streets;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "localities/" + getIdentifier();
    }
}
