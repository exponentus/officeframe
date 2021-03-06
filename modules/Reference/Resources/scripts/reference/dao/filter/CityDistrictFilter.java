package reference.dao.filter;

import com.exponentus.dataengine.IFilter;
import com.exponentus.scripting.WebFormData;
import reference.model.CityDistrict;
import reference.model.Locality;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class CityDistrictFilter implements IFilter<CityDistrict> {

    private Locality locality;

    public CityDistrictFilter(WebFormData params) {
        String localityId = params.getValueSilently("locality");
        if (!localityId.isEmpty()) {
            locality = new Locality();
            locality.setId(UUID.fromString(localityId));
        }
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    @Override
    public Predicate collectPredicate(Root<CityDistrict> root, CriteriaBuilder cb, Predicate condition) {

        if (locality != null) {
            if (condition == null) {
                condition = cb.equal(root.get("locality"), locality);
            } else {
                condition = cb.and(cb.equal(root.get("locality"), locality), condition);
            }
        }

        return condition;
    }
}
