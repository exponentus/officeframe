package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__delivery_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "DeliveryType.findAll", query = "SELECT m FROM DeliveryType AS m ORDER BY m.regDate")
public class DeliveryType extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "delivery-types/" + getIdentifier();
    }
}
