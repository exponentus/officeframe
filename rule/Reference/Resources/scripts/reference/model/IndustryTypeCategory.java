package reference.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("industryTypeCategory")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "ref__industry_type_categories", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class IndustryTypeCategory extends SimpleReferenceEntity {
}
