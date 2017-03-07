package staff.dao.filter;

import com.exponentus.runtimeobj.Filter;
import reference.model.OrgCategory;
import staff.model.OrganizationLabel;

import java.util.List;

public class OrganizationFilter extends Filter {

    private OrgCategory orgCategory;
    private List<OrganizationLabel> labels;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
