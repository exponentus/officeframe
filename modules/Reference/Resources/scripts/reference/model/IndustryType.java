package reference.model;

import administrator.dao.CollationDAO;
import administrator.model.Collation;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.dao.ActivityTypeCategoryDAO;
import reference.init.ModuleConst;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = ModuleConst.CODE + "__industry_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
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
        return ModuleConst.BASE_URL + "industry-types/" + getId();
    }

    @Override
    public IndustryType compose(_Session ses, Map<String, ?> data) {
        super.compose(ses, data);
        try {
            Map<String, String> categoryMap = (Map<String, String>) data.get("category");
            CollationDAO collationDAO = new CollationDAO(ses);
            Collation collation = collationDAO.findByExtKey(categoryMap.get("id"));
            ActivityTypeCategoryDAO activityTypeCategoryDAO = new ActivityTypeCategoryDAO(ses);
            ActivityTypeCategory category = activityTypeCategoryDAO.findById(collation.getIntKey());
            this.category = category;


        } catch (Exception e) {
            Lg.exception(e);
            return null;
        }
        return this;
    }
}
