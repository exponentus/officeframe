package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JsonRootName("roadRepairType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = AppConst.CODE + "__road_repair_type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class RoadRepairType extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "road-repair-types/" + getIdentifier();
    }
}
