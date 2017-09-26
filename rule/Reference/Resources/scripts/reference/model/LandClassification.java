package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@JsonRootName("landClassification")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__land_classifications")
public class LandClassification extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "land-classifications/" + getIdentifier();
    }
}
