package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("subdivision")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "subdivisions", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "Subdivision.findAll", query = "SELECT m FROM Subdivision AS m ORDER BY m.regDate")
public class Subdivision extends SimpleReferenceEntity {
}
