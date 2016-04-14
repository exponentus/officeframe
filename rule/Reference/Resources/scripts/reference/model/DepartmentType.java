package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleEntity;

@Entity
@Table(name = "department_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "DepartmentType.findAll", query = "SELECT m FROM DepartmentType AS m ORDER BY m.regDate")
public class DepartmentType extends SimpleEntity {

}
