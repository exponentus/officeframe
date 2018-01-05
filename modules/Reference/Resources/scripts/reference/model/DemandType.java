package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__demand_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "DemandType.findAll", query = "SELECT m FROM DemandType AS m ORDER BY m.regDate")
public class DemandType extends SimpleReferenceEntity {
    public String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "demand-types/" + getIdentifier();
    }
}
