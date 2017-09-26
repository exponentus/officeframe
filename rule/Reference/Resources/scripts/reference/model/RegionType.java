package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;
import reference.model.constants.RegionCode;

import javax.persistence.*;

@JsonRootName("regionType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__region_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "code"}))
@NamedQuery(name = "RegionType.findAll", query = "SELECT m FROM RegionType AS m ORDER BY m.regDate")
public class RegionType extends SimpleReferenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 32)
    private RegionCode code = RegionCode.UNKNOWN;

    public RegionCode getCode() {
        return code;
    }

    public void setCode(RegionCode code) {
        this.code = code;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "region-types/" + getIdentifier();
    }
}
