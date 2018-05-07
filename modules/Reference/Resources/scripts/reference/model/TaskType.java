package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = ModuleConst.CODE + "__task_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
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
        return ModuleConst.BASE_URL + "task-types/" + getId();
    }
}
