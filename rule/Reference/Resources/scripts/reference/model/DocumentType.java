package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;

@Entity
@Table(name = "document_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "category" }))
@NamedQuery(name = "DocumentType.findAll", query = "SELECT m FROM DocumentType AS m ORDER BY m.regDate")
public class DocumentType extends SimpleReferenceEntity {

	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
