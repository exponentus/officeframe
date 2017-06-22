package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("nationality")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "ref__nationalities")
@Cacheable(true)
public class Nationality extends SimpleReferenceEntity {

}
