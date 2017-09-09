package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.model.constants.RealEstateObjStatusCode;

import javax.persistence.*;

@JsonRootName("realEstateObjStatus")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__real_estate_obj_statuses", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "code"}))
public class RealEstateObjStatus extends SimpleReferenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 7, unique = true)
    private RealEstateObjStatusCode code = RealEstateObjStatusCode.UNKNOWN;

    public RealEstateObjStatusCode getCode() {
        return code;
    }

    public void setCode(RealEstateObjStatusCode code) {
        this.code = code;
    }
}
