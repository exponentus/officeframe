package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonRootName("locality")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "localities", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type_id", "region_id"}))
@NamedQuery(name = "Locality.findAll", query = "SELECT m FROM Locality AS m ORDER BY m.regDate")
public class Locality extends SimpleReferenceEntity {

    @JsonIgnore
    @OneToMany(mappedBy = "locality")
    private List<Street> streets;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private LocalityType type;

    //	@JsonIgnore
    @NotNull
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private District district;

    //	@JsonIgnore
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

    public List<Street> getStreets() {
        return streets;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setStreets(List<Street> streets) {
        this.streets = streets;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @Override
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append(super.getFullXMLChunk(ses));
        chunk.append("<localitytype id=\"" + type.getId() + "\">" + type.getLocName(ses.getLang()) + "</localitytype>");
        chunk.append("<region id=\"" + region.getId() + "\">" + region.getLocName(ses.getLang()) + "</region>");
        if (district != null) {
            chunk.append(
                    "<district id=\"" + district.getId() + "\">" + district.getLocName(ses.getLang()) + "</district>");
        }
        return chunk.toString();
    }
}
