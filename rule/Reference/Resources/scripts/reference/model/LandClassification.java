package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;

@JsonRootName("landClassification")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__land_classifications")
public class LandClassification extends SimpleReferenceEntity {


}
