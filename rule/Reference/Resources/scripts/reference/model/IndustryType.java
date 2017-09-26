package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@JsonRootName("industryType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "ref__industry_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class IndustryType extends SimpleReferenceEntity {

    private IndustryTypeCategory category;

    public IndustryTypeCategory getCategory() {
        return category;
    }

    public void setCategory(IndustryTypeCategory category) {
        this.category = category;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "industry-types/" + getIdentifier();
    }
}
