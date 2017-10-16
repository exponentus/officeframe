package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;

@JsonRootName("routeClassification")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__route_classifications", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class RouteClassification extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "route-classifications/" + getIdentifier();
    }
}
