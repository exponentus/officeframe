package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * @author Kayra created 07-06-2016
 */

@JsonRootName("requestType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__request_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "RequestType.findAll", query = "SELECT m FROM RequestType AS m ORDER BY m.regDate")
public class RequestType extends SimpleReferenceEntity {
}
