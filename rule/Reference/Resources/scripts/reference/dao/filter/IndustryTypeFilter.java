package reference.dao.filter;

import com.exponentus.runtimeobj.IFilter;
import reference.model.ActivityTypeCategory;
import reference.model.IndustryType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class IndustryTypeFilter implements IFilter<IndustryType> {

    private ActivityTypeCategory activityTypeCategory;

    public ActivityTypeCategory getActivityTypeCategory() {
        return activityTypeCategory;
    }

    public void setActivityTypeCategory(ActivityTypeCategory activityTypeCategory) {
        this.activityTypeCategory = activityTypeCategory;
    }

    @Override
    public Predicate collectPredicate(Root<IndustryType> root, CriteriaBuilder cb, Predicate condition) {

        if (activityTypeCategory != null) {
            if (condition == null) {
                condition = cb.and(cb.equal(root.get("category"), activityTypeCategory));
            } else {
                condition = cb.and(cb.equal(root.get("category"), activityTypeCategory), condition);
            }
        }

        return condition;
    }
}
