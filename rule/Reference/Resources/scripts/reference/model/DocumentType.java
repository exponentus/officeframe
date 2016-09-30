package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;

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

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<category>" + category + "</category>");
		return chunk.toString();
	}

}
