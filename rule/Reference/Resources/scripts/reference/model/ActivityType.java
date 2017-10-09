package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JsonRootName("activityType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "ref__activity_type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class ActivityType extends SimpleReferenceEntity {

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "activity-types/" + getIdentifier();
    }
}
