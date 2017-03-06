package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("departmenttype")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "department_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "DepartmentType.findAll", query = "SELECT m FROM DepartmentType AS m ORDER BY m.regDate")
public class DepartmentType extends SimpleReferenceEntity {
}
