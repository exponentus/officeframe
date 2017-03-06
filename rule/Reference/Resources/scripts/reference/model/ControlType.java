package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("controltype")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "control_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "ControlType.findAll", query = "SELECT m FROM ControlType AS m ORDER BY m.regDate")
public class ControlType extends SimpleReferenceEntity {

    @Column(length = 16, unique = true)
    private String code;

    private String category;

    @Column(name = "default_hours")
    private int defaultHours;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDefaultHours() {
        return defaultHours;
    }

    public void setDefaultHours(int defaultHours) {
        this.defaultHours = defaultHours;
    }

    @Override
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append(super.getFullXMLChunk(ses));
        chunk.append("<defaulthours>" + defaultHours + "</defaulthours>");
        return chunk.toString();
    }
}
