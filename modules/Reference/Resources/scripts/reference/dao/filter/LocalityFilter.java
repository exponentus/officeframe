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
    private Boolean isDistrictCenter;

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
        if (params.containsField("isDistrictCenter")) {
            isDistrictCenter = params.getBoolSilently("isDistrictCenter");
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

    public boolean isDistrictCenter() {
        return isDistrictCenter;
    }

    public void setDistrictCenter(boolean districtCenter) {
        isDistrictCenter = districtCenter;
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

        if (isDistrictCenter != null) {
            if (condition == null) {
                if (isDistrictCenter) {
                    condition = cb.isTrue(root.get("isDistrictCenter"));
                } else {
                    condition = cb.isFalse(root.get("isDistrictCenter"));
                }
            } else {
                if (isDistrictCenter) {
                    condition = cb.and(cb.isTrue(root.get("isDistrictCenter")), condition);
                } else {
                    condition = cb.and(cb.isFalse(root.get("isDistrictCenter")), condition);
                }
            }
        }

        return condition;
    }
}
