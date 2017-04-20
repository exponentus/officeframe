package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("structuretype")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__structure_type", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "StructureType.findAll", query = "SELECT m FROM StructureType AS m ORDER BY m.regDate")
public class StructureType extends SimpleReferenceEntity {
}
