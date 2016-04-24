package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleEntity;

@Entity
@Table(name = "dispute_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }) )
@NamedQuery(name = "DisputeType.findAll", query = "SELECT m FROM DisputeType AS m ORDER BY m.regDate")
public class ClaimantDecisionType extends SimpleEntity {

}
