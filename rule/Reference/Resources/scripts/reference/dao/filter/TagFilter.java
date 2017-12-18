package reference.dao.filter;

import com.exponentus.runtimeobj.IFilter;
import reference.model.Tag;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TagFilter implements IFilter<Tag> {

    private String category;
    private boolean withHidden = true;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isWithHidden() {
        return withHidden;
    }

    public void setWithHidden(boolean withHidden) {
        this.withHidden = withHidden;
    }

    @Override
    public Predicate collectPredicate(Root<Tag> root, CriteriaBuilder cb, Predicate condition) {

        if (category != null && !category.isEmpty()) {
            if (condition == null) {
                condition = cb.or(cb.equal(root.get("category"), ""), cb.like(cb.lower(root.<String>get("category")), category));
            } else {
                condition = cb.or(cb.equal(root.get("category"), ""), cb.like(cb.lower(root.<String>get("category")), category), condition);
            }
        }

        if (!withHidden) {
            if (condition == null) {
                condition = cb.isFalse(root.get("hidden"));
            } else {
                condition = cb.and(cb.isFalse(root.get("hidden")), condition);
            }
        }

        return condition;
    }
}
