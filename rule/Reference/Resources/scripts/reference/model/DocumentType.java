package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__document_types", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category"}))
@NamedQuery(name = "DocumentType.findAll", query = "SELECT m FROM DocumentType AS m ORDER BY m.regDate")
public class DocumentType extends SimpleReferenceEntity {

    public String prefix;

    @Column(length = 128, nullable = false)
    private String category = "";

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "document-types/" + getId();
    }
}
