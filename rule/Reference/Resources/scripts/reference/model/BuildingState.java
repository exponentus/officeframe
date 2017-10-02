package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JsonRootName("buildingState")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "ref__building_states", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class BuildingState extends SimpleReferenceEntity {


    @Column(name="require_date")
    private boolean requireDate;


    public boolean isRequireDate() {
        return requireDate;
    }

    public void setRequireDate(boolean requireDate) {
        this.requireDate = requireDate;
    }


}
