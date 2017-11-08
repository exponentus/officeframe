package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;
import java.util.List;

@JsonRootName("region")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__regions", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "country_id"}))
@NamedQuery(name = "Region.findAll", query = "SELECT m FROM Region AS m ORDER BY m.regDate")
public class Region extends SimpleReferenceEntity {

    @JsonIgnore
    @OneToMany(mappedBy = "region")
    @OrderBy("name ASC")
    private List<District> districts;

    @JsonIgnore
    @OneToMany(mappedBy = "region")
    @OrderBy("name ASC")
    private List<Locality> localities;

    // @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Country country;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RegionType type;

    @Column(name = "is_primary")
    private boolean isPrimary;

    @Column(name = "org_coordinates")
    private String orgCoordinates;

    public RegionType getType() {
        return type;
    }

    public void setType(RegionType type) {
        this.type = type;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public List<Locality> getLocalities() {
        return localities;
    }

    public void setLocalities(List<Locality> localities) {
        this.localities = localities;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getOrgCoordinates() {
        return orgCoordinates;
    }

    public void setOrgCoordinates(String orgCoordinates) {
        this.orgCoordinates = orgCoordinates;
    }


    @Override
    public String getURL() {
        return AppConst.BASE_URL + "regions/" + getIdentifier();
    }
}
