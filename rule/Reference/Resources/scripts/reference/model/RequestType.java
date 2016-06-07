package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;

/**
 * 
 * 
 * @author Kayra created 07-06-2016
 */

@Entity
@Table(name = "request_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }) )
@NamedQuery(name = "RequestType.findAll", query = "SELECT m FROM RequestType AS m ORDER BY m.regDate")
public class RequestType extends SimpleReferenceEntity {

}
