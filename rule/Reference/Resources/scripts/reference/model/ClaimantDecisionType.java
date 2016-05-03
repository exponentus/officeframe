package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;

@Entity
@Table(name = "claimant_decision_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }) )
@NamedQuery(name = "ClaimantDecisionType.findAll", query = "SELECT m FROM ClaimantDecisionType AS m ORDER BY m.regDate")
public class ClaimantDecisionType extends SimpleReferenceEntity {

}
