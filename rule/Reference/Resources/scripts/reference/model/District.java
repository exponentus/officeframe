package reference.model;

import administrator.dao.CollationDAO;
import administrator.model.Collation;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.converter.ListOfStringArrayConverter;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.dao.CountryDAO;
import reference.dao.RegionDAO;
import reference.dao.RegionTypeDAO;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonRootName("district")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__districts", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "region_id"}))
@NamedQuery(name = "District.findAll", query = "SELECT m FROM District AS m ORDER BY m.regDate")
public class District extends SimpleReferenceEntity {
    private List<Locality> localities;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Region region;
    @Convert(converter = ListOfStringArrayConverter.class)
    @Column(name = "lat_lng", columnDefinition = "jsonb")
    private List<String[]> latLng = new ArrayList<>();

    @OneToMany(mappedBy = "district")
    @OrderBy("name ASC")
    public List<Locality> getLocalities() {
        return localities;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<String[]> getLatLng() {
        return latLng;
    }

    public void setLatLng(List<String[]> latLng) {
        this.latLng = latLng;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "districts/" + getId();
    }

    @Override
    public void compose(_Session ses, Map<String, ?> data) {
        super.compose(ses, data);
        try {
            Map<String,String> regionMap = (Map<String,String>)data.get("region");
            CollationDAO collationDAO = new CollationDAO(ses);
            Collation collation = collationDAO.findByExtKey((String)regionMap.get("id"));
            RegionDAO regionDAO = new RegionDAO(ses);
            Region region = regionDAO.findById(collation.getIntKey());
            this.region = region;

        }catch (Exception e){
            Lg.exception(e);
        }
    }
}
