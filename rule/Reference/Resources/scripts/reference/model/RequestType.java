package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

/**
 * @author Kayra created 07-06-2016
 */

@JsonRootName("requesttype")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "request_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "RequestType.findAll", query = "SELECT m FROM RequestType AS m ORDER BY m.regDate")
public class RequestType extends SimpleReferenceEntity {
}
