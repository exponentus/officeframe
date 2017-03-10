package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("worktype")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "work_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "WorkType.findAll", query = "SELECT m FROM WorkType AS m ORDER BY m.regDate")
public class WorkType extends SimpleReferenceEntity {
}
