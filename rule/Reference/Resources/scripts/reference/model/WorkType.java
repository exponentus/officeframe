package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.ModuleConst;

import javax.persistence.*;
import java.util.List;

@JsonRootName("workType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__work_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "WorkType.findAll", query = "SELECT m FROM WorkType AS m ORDER BY m.regDate")
public class WorkType extends SimpleReferenceEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private WorkType parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<WorkType> children;

    public WorkType getParent() {
        return parent;
    }

    public void setParent(WorkType parent) {
        this.parent = parent;
    }

    public List<WorkType> getChildren() {
        return children;
    }

    public void setChildren(List<WorkType> children) {
        this.children = children;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "work-types/" + getId();
    }
}
