package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__org_categories", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "OrgCategory.findAll", query = "SELECT m FROM OrgCategory AS m ORDER BY m.regDate")
public class OrgCategory extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "org-categories/" + getId();
    }
}
