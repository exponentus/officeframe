package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;

/**
 *
 *
 * @author Kayra created 11-11-2016
 */

@Entity
@Table(name = "demand_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "DemandType.findAll", query = "SELECT m FROM DemandType AS m ORDER BY m.regDate")
public class DemandType extends SimpleReferenceEntity {
	
}
