package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;

/**
 *
 *
 * @author Kayra created 07-01-2016
 */

@Entity
@Cacheable(true)
@Table(name = "property_codes", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "PropertyCode.findAll", query = "SELECT m FROM PropertyCode AS m ORDER BY m.regDate")
public class PropertyCode extends SimpleReferenceEntity {
	
}
