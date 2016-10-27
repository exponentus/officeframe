package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;

@Entity
@Table(name = "document_subjs", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "category" }))
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

}
