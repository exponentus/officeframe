package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;

@Entity
@Table(name = "unit_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }) )
@NamedQuery(name = "UnitType.findAll", query = "SELECT m FROM UnitType AS m ORDER BY m.regDate")
public class UnitType extends SimpleReferenceEntity {

}
