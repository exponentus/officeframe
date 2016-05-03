package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;

@Entity
@Table(name = "org_categories", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "OrgCategory.findAll", query = "SELECT m FROM OrgCategory AS m ORDER BY m.regDate")
public class OrgCategory extends SimpleReferenceEntity {

}
