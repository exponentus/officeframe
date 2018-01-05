package staff.dao.filter;

import com.exponentus.runtimeobj.Filter;
import staff.model.IndividualLabel;

import java.util.List;

public class IndividualFilter extends Filter {

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
}
