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

@JsonRootName("textTemplate")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__text_templates", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "category" }))
@NamedQuery(name = "TextTemplate.findAll", query = "SELECT m FROM TextTemplate AS m ORDER BY m.regDate")
public class TextTemplate extends SimpleReferenceEntity {

	@Column(length = 128, nullable = false)
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
