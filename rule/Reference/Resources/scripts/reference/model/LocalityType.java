package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;
import reference.model.constants.LocalityCode;

import javax.persistence.*;

@JsonRootName("localityType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__locality_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "code"}))
@NamedQuery(name = "LocalityType.findAll", query = "SELECT m FROM LocalityType AS m ORDER BY m.regDate")
public class LocalityType extends SimpleReferenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 32)
    private LocalityCode code = LocalityCode.UNKNOWN;

    public LocalityCode getCode() {
        return code;
    }

    public void setCode(LocalityCode code) {
        this.code = code;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "locality-types/" + getIdentifier();
    }
}
