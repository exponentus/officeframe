package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("nationality")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "ref__nationalities", uniqueConstraints = @UniqueConstraint(columnNames = { "loc_name" }))
@Cacheable(true)
public class Nationality extends SimpleReferenceEntity {

}
