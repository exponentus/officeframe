package reference.model;

import administrator.dao.CollationDAO;
import administrator.model.Collation;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.dao.CountryDAO;
import reference.dao.LocalityTypeDAO;
import reference.dao.RegionDAO;
import reference.dao.RegionTypeDAO;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

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

    public void setStreets(List<Street> streets) {
        this.streets = streets;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public boolean compose(_Session ses, Map<String, ?> data) {
        super.compose(ses, data);
        try {
            Map<String,String> countryMap = (Map<String,String>)data.get("region");
            CollationDAO collationDAO = new CollationDAO(ses);
            Collation collation = collationDAO.findByExtKey((String)countryMap.get("id"));
            RegionDAO dao = new RegionDAO(ses);
            Region region = dao.findById(collation.getIntKey());
            this.region = region;

            Map<String,String> typeMap = (Map<String,String>)data.get("type");
            collation = collationDAO.findByExtKey((String)typeMap.get("id"));
            LocalityTypeDAO localityTypeDAO = new LocalityTypeDAO(ses);
            LocalityType localityType = localityTypeDAO.findById(collation.getIntKey());
            type = localityType;

        }catch (Exception e){
            Lg.exception(e);
            return false;
        }
        return true;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "localities/" + getId();
    }
}
