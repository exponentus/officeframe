package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("documentType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__document_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "category" }))
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
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<category>" + category + "</category>");
		chunk.append("<prefix>" + prefix + "</prefix>");
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
