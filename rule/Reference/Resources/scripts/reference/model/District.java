package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.converter.ListOfStringArrayConverter;
import com.exponentus.common.model.converter.LocalizedValConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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


    @Convert(converter = ListOfStringArrayConverter.class)
    @Column(name = "lat_lng", columnDefinition = "jsonb")
    public List<String[]> latLng = new ArrayList<>();


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
        return AppConst.BASE_URL + "districts/" + getIdentifier();
    }
}
