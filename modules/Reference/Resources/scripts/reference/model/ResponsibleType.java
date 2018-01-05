package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__responsible_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "ResponsibleType.findAll", query = "SELECT m FROM ResponsibleType AS m ORDER BY m.regDate")
public class ResponsibleType extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "responsible-types/" + getIdentifier();
    }
}
