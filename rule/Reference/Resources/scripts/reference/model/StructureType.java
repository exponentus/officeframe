package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("structuretype")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "structure_type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "StructureType.findAll", query = "SELECT m FROM StructureType AS m ORDER BY m.regDate")
public class StructureType extends SimpleReferenceEntity {
}
