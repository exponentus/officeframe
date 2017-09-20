package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;

@JsonRootName("realEstateObjStatus")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__real_estate_obj_statuses", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "code"}))
public class RealEstateObjStatus extends SimpleReferenceEntity {


}
