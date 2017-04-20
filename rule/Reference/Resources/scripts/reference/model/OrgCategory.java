package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("orgcategory")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__org_categories", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "OrgCategory.findAll", query = "SELECT m FROM OrgCategory AS m ORDER BY m.regDate")
public class OrgCategory extends SimpleReferenceEntity {
}
