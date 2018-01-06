package staff.dao.filter;

import com.exponentus.dataengine.IFilter;
import reference.model.OrgCategory;
import staff.model.Organization;
import staff.model.OrganizationLabel;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class OrganizationFilter implements IFilter<Organization> {

    private OrgCategory orgCategory;
    private List<OrganizationLabel> labels;
    private String keyword;

    public OrganizationFilter() {
    }

    public OrganizationFilter(OrgCategory orgCategory, List<OrganizationLabel> labels) {
        this.orgCategory = orgCategory;
        this.labels = labels;
    }

    public OrgCategory getOrgCategory() {
        return orgCategory;
    }

    public void setOrgCategory(OrgCategory orgCategory) {
        this.orgCategory = orgCategory;
    }

    public List<OrganizationLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<OrganizationLabel> labels) {
        this.labels = labels;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Predicate collectPredicate(Root<Organization> root, CriteriaBuilder cb, Predicate condition) {
        if (orgCategory != null) {
            if (condition == null) {
                condition = cb.equal(root.get("orgCategory"), orgCategory);
            } else {
                condition = cb.and(cb.equal(root.get("orgCategory"), orgCategory), condition);
            }
        }

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
