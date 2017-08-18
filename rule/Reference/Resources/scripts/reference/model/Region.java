package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import java.util.List;

@JsonRootName("region")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__regions", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "country_id"}))
@NamedQuery(name = "Region.findAll", query = "SELECT m FROM Region AS m ORDER BY m.regDate")
public class Region extends SimpleReferenceEntity {

    @JsonIgnore
    @OneToMany(mappedBy = "region")
    @OrderBy("name ASC")
    private List<District> districts;

    // @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Country country;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RegionType type;

    public RegionType getType() {
        return type;
    }

    public void setType(RegionType type) {
        this.type = type;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }

    @Override
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append(super.getFullXMLChunk(ses));
        if (type != null && type.getId() != null) {
            chunk.append("<type id=\"" + type.getId() + "\">" + type.getLocName(ses.getLang()) + "</type>");
        }
        if (country != null) {
            chunk.append("<country id=\"" + country.getId() + "\">" + country.getLocName(ses.getLang()) + "</country>");
        }
        return chunk.toString();
    }

    // TODO consistency between Region and Country ??

    @Override
    public String getShortXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append("<name>" + getName() + "</name>");
        try {
            chunk.append("<type id=\"" + type.getId() + "\">" + type.getLocName(ses.getLang()) + "</type>");
            if (country != null) {
                chunk.append(
                        "<country id=\"" + country.getId() + "\">" + country.getLocName(ses.getLang()) + "</country>");
            }
        } catch (Exception e) {
            Server.logger.exception(e);
        }
        return chunk.toString();
    }
}
