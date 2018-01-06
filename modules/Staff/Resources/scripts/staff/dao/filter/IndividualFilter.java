package staff.dao.filter;

import com.exponentus.dataengine.IFilter;
import staff.model.Individual;
import staff.model.IndividualLabel;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class IndividualFilter implements IFilter<Individual> {

    private List<IndividualLabel> labels;
    private String keyword;

    public IndividualFilter() {
    }

    public IndividualFilter(List<IndividualLabel> labels) {
        this.labels = labels;
    }

    public List<IndividualLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<IndividualLabel> labels) {
        this.labels = labels;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Predicate collectPredicate(Root<Individual> root, CriteriaBuilder cb, Predicate condition) {

        if (labels != null && !labels.isEmpty()) {
            if (condition == null) {
                condition = root.get("labels").in(labels);
            } else {
                condition = cb.and(root.get("labels").in(labels), condition);
            }
        }

        if (keyword != null && !keyword.isEmpty()) {
            if (condition == null) {
                condition = cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
            } else {
                condition = cb.and(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"), condition);
            }
        }

        return condition;
    }
}
