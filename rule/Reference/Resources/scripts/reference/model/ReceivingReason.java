package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import kz.lof.common.model.SimpleEntity;

/**
 * 
 * 
 * @author Kayra created 07-01-2016
 */

@Entity
@Table(name = "receiving_reason", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "ReceivingReason.findAll", query = "SELECT m FROM ReceivingReason AS m ORDER BY m.regDate")
public class ReceivingReason extends SimpleEntity {

}
