package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "districts/" + getIdentifier();
    }
}
