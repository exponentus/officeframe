package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("buildingMaterial")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "ref__building_materials", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "BuildingMaterial.findAll", query = "SELECT m FROM BuildingMaterial AS m ORDER BY m.regDate")
public class BuildingMaterial extends SimpleReferenceEntity {
}
