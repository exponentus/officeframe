package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;

@JsonRootName("unitType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__unit_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "UnitType.findAll", query = "SELECT m FROM UnitType AS m ORDER BY m.regDate")
public class UnitType extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "unit-types/" + getIdentifier();
    }
}
