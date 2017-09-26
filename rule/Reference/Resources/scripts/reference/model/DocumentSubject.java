package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.init.AppConst;

import javax.persistence.*;

@JsonRootName("documentSubject")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__document_subjs", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category"}))
@NamedQuery(name = "DocumentSubject.findAll", query = "SELECT m FROM DocumentSubject AS m ORDER BY m.regDate")
public class DocumentSubject extends SimpleReferenceEntity {

    @Column(length = 128, nullable = false)
    private String category = "";

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "document-subjects/" + getIdentifier();
    }
}
