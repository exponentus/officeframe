package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import kz.lof.common.model.SimpleEntity;

@Entity
@Table(name = "structure_type", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "StructureType.findAll", query = "SELECT m FROM StructureType AS m ORDER BY m.regDate")
public class StructureType extends SimpleEntity {

}
