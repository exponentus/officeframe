package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("claimantDecisionType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__claimant_decision_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "ClaimantDecisionType.findAll", query = "SELECT m FROM ClaimantDecisionType AS m ORDER BY m.regDate")
public class ClaimantDecisionType extends SimpleReferenceEntity {
}
