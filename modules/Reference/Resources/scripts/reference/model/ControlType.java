package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;
import reference.model.constants.ControlSchemaType;
import reference.model.constants.converter.ControlSchemaTypeConverter;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__control_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "ControlType.findAll", query = "SELECT m FROM ControlType AS m ORDER BY m.regDate")
public class ControlType extends SimpleReferenceEntity {

    @Column(length = 16, unique = true)
    private String code;

    @Convert(converter = ControlSchemaTypeConverter.class)
    private ControlSchemaType schema;

    private String category;

    @Column(name = "default_hours")
    private int defaultHours;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ControlSchemaType getSchema() {
        return schema;
    }

    public void setSchema(ControlSchemaType schema) {
        this.schema = schema;
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
    public String getURL() {
        return ModuleConst.BASE_URL + "control-types/" + getIdentifier();
    }
}
