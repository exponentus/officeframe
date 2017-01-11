package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;

@Entity
@Cacheable(true)
@Table(name = "delivery_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "DeliveryType.findAll", query = "SELECT m FROM DeliveryType AS m ORDER BY m.regDate")
public class DeliveryType extends SimpleReferenceEntity {
	
}
