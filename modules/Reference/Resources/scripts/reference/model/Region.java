package reference.model;

import administrator.dao.CollationDAO;
import administrator.model.Collation;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.dao.CountryDAO;
import reference.dao.RegionTypeDAO;
import reference.init.ModuleConst;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "__regions", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "country_id"}))
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

    @ManyToOne
    @JoinColumn(nullable = false)
    private Country country;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RegionType type;

    @Column(name = "is_primary")
    private boolean isPrimary;

    @Column(length = 128)
    private String coordinates;

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

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "regions/" + getId();
    }

    @Override
    public boolean compose(_Session ses, Map<String, ?> data) {
        super.compose(ses, data);
        try {
            Map<String, String> countryMap = (Map<String, String>) data.get("country");
            CollationDAO collationDAO = new CollationDAO(ses);
            Collation collation = collationDAO.findByExtKey(countryMap.get("id"));
            CountryDAO countryDAO = new CountryDAO(ses);
            Country country = countryDAO.findById(collation.getIntKey());
            this.country = country;

            Map<String, String> typeMap = (Map<String, String>) data.get("type");
            collation = collationDAO.findByExtKey(typeMap.get("id"));
            RegionTypeDAO regionTypeDAO = new RegionTypeDAO(ses);
            RegionType regionType = regionTypeDAO.findById(collation.getIntKey());
            this.type = regionType;

            //          String c = (String) data.get("code");
            //          code = RegionCode.valueOf(c);
        } catch (Exception e) {
            Lg.exception(e);
            return false;
        }
        return true;
    }
}
