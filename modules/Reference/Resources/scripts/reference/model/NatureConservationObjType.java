package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "__nature_conservation_obj_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class NatureConservationObjType extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "nature-conservation-obj-types/" + getId();
    }
}
