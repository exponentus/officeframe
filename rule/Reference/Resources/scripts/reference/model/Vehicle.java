package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("vehicle")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "vehicles", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "Vehicle.findAll", query = "SELECT m FROM Vehicle AS m ORDER BY m.regDate")
public class Vehicle extends SimpleReferenceEntity {

}
