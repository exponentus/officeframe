package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonRootName("taskType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__task_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "TaskType.findAll", query = "SELECT m FROM TaskType AS m ORDER BY m.regDate")
public class TaskType extends SimpleReferenceEntity {

    @Column(length = 140)
    public String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "task-types/" + getIdentifier();
    }
}
