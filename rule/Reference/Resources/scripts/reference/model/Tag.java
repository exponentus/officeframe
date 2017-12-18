package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.converter.UUIDConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import reference.init.AppConst;

import javax.persistence.*;
import java.util.UUID;

@JsonRootName("tag")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = AppConst.CODE + "__tags", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "color"}))
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
@NamedQuery(name = "Tag.findAll", query = "SELECT m FROM Tag AS m WHERE m.parent IS NULL ORDER BY m.name")
public class Tag extends SimpleReferenceEntity {

    @Column(length = 7)
    private String color;

    @JsonIgnore
    @Convert("uuidConverter")
    @Basic(fetch = FetchType.LAZY, optional = true)
    private Tag parent;

    private boolean hidden;

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public UUID getParentId() {
        if (parent == null) {
            return null;
        }
        return parent.id;
    }

    public void setParentId(UUID id) {
        if (id == null) {
            setParent(null);
            return;
        }

        Tag parent = new Tag();
        parent.setId(id);
        setParent(parent);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "tags/" + getId();
    }

    @Override
    public String toString() {
        return "Tag[" + id + ", " + getLocName() + ", " + color + ", " + parent + ", " + getRegDate() + "]";
    }
}
