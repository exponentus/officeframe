package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleEntity;

@Entity
@Table(name = "subdivisions", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "Subdivision.findAll", query = "SELECT m FROM Subdivision AS m ORDER BY m.regDate")
public class Subdivision extends SimpleEntity {

}
