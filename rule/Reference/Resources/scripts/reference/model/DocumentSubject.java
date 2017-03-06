package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("documentsubject")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "document_subjs", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category"}))
@NamedQuery(name = "DocumentSubject.findAll", query = "SELECT m FROM DocumentSubject AS m ORDER BY m.regDate")
public class DocumentSubject extends SimpleReferenceEntity {

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getFullXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append(super.getFullXMLChunk(ses));
        chunk.append("<category>" + category + "</category>");
        return chunk.toString();
    }

    @Override
    public String getShortXMLChunk(_Session ses) {
        StringBuilder chunk = new StringBuilder(1000);
        chunk.append("<name>" + name + "</name>");
        chunk.append("<category>" + category + "</category>");
        return chunk.toString();
    }
}
