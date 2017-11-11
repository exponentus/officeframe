package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JsonRootName("natureConservationObjType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__nature_conservation_obj_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class NatureConservationObjType extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "nature-conservation-obj-types/" + getId();
    }
}
