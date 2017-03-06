package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

/**
 * @author Kayra created 07-01-2016
 */

@JsonRootName("receivingreason")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "receiving_reason", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "ReceivingReason.findAll", query = "SELECT m FROM ReceivingReason AS m ORDER BY m.regDate")
public class ReceivingReason extends SimpleReferenceEntity {
}
