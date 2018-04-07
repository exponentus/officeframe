package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;
import reference.model.constants.CountryCode;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

// https://en.wikipedia.org/wiki/ISO_3166-1

// a2 - alpha code 2
// a3 - alpha code 3
// n3 // numeric code 3

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "__countries", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "code"}))
@NamedQuery(name = "Country.findAll", query = "SELECT m FROM Country AS m ORDER BY m.regDate")
public class Country extends SimpleReferenceEntity {

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    @OrderBy("name ASC")
    private List<Region> regions;

    @FTSearchable
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7, unique = true)
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

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "countries/" + getId();
    }

    @Override
    public boolean compose(_Session ses, Map<String, ?> data) {
        if (super.compose(ses, data)) {
            try {
                code = CountryCode.valueOf((String) data.get("code"));
                return true;
            } catch (Exception e) {
                Lg.exception(e);
            }
        }
        return false;
    }

}
