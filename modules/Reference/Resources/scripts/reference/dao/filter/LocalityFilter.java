package reference.dao.filter;

import com.exponentus.dataengine.IFilter;
import com.exponentus.scripting.WebFormData;
import reference.model.District;
import reference.model.Locality;
import reference.model.Region;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class LocalityFilter implements IFilter<Locality> {

    private Region region;
    private District district;

    public LocalityFilter(WebFormData params) {
        String regionId = params.getValueSilently("region");
        if (!regionId.isEmpty()) {
            region = new Region();
            region.setId(UUID.fromString(regionId));
        }
        String districtId = params.getValueSilently("district");
        if (!districtId.isEmpty()) {
            district = new District();
            district.setId(UUID.fromString(districtId));
        }
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @Override
    public Predicate collectPredicate(Root<Locality> root, CriteriaBuilder cb, Predicate condition) {

        if (region != null) {
            if (condition == null) {
                condition = cb.equal(root.get("region"), region);
            } else {
                condition = cb.and(cb.equal(root.get("region"), region), condition);
            }
        }

        if (district != null) {
            if (condition == null) {
                condition = cb.equal(root.get("district"), district);
            } else {
                condition = cb.and(cb.equal(root.get("district"), district), condition);
            }
        }

        return condition;
    }
}
