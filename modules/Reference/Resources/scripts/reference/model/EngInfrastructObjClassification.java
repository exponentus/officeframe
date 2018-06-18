package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "__eng_infrastruct_obj_classifications", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "EngInfrastructObjClassification.findAll", query = "SELECT m FROM EngInfrastructObjClassification AS m ORDER BY m.regDate")
public class EngInfrastructObjClassification extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "eng-infrastruct-obj-classifications/" + getId();
    }
}
