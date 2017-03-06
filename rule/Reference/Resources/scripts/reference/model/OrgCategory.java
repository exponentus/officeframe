package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("orgcategory")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "org_categories", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "OrgCategory.findAll", query = "SELECT m FROM OrgCategory AS m ORDER BY m.regDate")
public class OrgCategory extends SimpleReferenceEntity {
}
