package staff.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import java.util.List;

@JsonRootName("individual")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "staff__individuals", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "bin"}))
public class Individual extends SimpleReferenceEntity {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "staff__individuals_labels",
            joinColumns = @JoinColumn(name = "individual_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
    private List<IndividualLabel> labels;

    @FTSearchable
    @Column(length = 12)
    private String bin = "";

    public List<IndividualLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<IndividualLabel> labels) {
        this.labels = labels;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }
}
