package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "ref__memorial_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class MemorialType extends SimpleReferenceEntity {
}
