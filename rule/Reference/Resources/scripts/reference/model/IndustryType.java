package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JsonRootName("industryType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = AppConst.CODE + "__industry_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class IndustryType extends SimpleReferenceEntity {

    @NotNull
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private ActivityTypeCategory category;

    public ActivityTypeCategory getCategory() {
        return category;
    }

    public void setCategory(ActivityTypeCategory category) {
        this.category = category;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "industry-types/" + getId();
    }
}
