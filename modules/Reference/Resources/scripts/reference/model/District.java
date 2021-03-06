package reference.model;

import administrator.dao.CollationDAO;
import administrator.model.Collation;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import reference.dao.RegionDAO;
import reference.init.ModuleConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

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

    @Column(name = "lat_lng", columnDefinition = "TEXT")
    private String latLng;

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

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "districts/" + getId();
    }

    @Override
    public boolean compose(_Session ses, Map<String, ?> data) {
        if (super.compose(ses, data)) {
            try {
                Map<String, String> regionMap = (Map<String, String>) data.get("region");
                CollationDAO collationDAO = new CollationDAO(ses);
                Collation collation = collationDAO.findByExtKey(regionMap.get("id"));
                RegionDAO regionDAO = new RegionDAO(ses);
                Region region = regionDAO.findById(collation.getIntKey());
                this.region = region;
                return true;
            } catch (Exception e) {
                Lg.exception(e);
            }
        }
        return false;
    }
}
