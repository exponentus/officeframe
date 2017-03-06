package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("deliverytype")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "delivery_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "DeliveryType.findAll", query = "SELECT m FROM DeliveryType AS m ORDER BY m.regDate")
public class DeliveryType extends SimpleReferenceEntity {
}
