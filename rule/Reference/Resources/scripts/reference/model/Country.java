package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.model.constants.CountryCode;

import javax.persistence.*;
import java.util.List;

// https://en.wikipedia.org/wiki/ISO_3166-1

// a2 - alpha code 2
// a3 - alpha code 3
// n3 // numeric code 3

@JsonRootName("country")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__countries", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "code"}))
@NamedQuery(name = "Country.findAll", query = "SELECT m FROM Country AS m ORDER BY m.regDate")
public class Country extends SimpleReferenceEntity {

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    @OrderBy("name ASC")
    private List<Region> regions;

    @FTSearchable
    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 7, unique = true)
    private CountryCode code = CountryCode.UNKNOWN;

    public CountryCode getCode() {
        return code;
    }

    public void setCode(CountryCode code) {
        this.code = code;
    }

    @JsonIgnore
    public List<Region> getRegions() {
        return regions;
    }

    @JsonIgnore
    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    @JsonIgnore
    @Override
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append(super.getFullXMLChunk(ses));
        chunk.append("<code>" + code + "</code>");
        return chunk.toString();
    }
}
