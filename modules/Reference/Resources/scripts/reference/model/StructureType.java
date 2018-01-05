package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__structure_type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "StructureType.findAll", query = "SELECT m FROM StructureType AS m ORDER BY m.regDate")
public class StructureType extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "structure-types/" + getIdentifier();
    }
}
