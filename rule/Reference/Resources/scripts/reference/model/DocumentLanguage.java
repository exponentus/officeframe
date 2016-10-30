package reference.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "document_languages", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "code" }))
@NamedQuery(name = "DocumentLanguage.findAll", query = "SELECT m FROM DocumentLanguage AS m ORDER BY m.regDate")
public class DocumentLanguage extends SimpleReferenceEntity {

	@Enumerated(EnumType.STRING)
	@Column(nullable = true, length = 7, unique = true)
	private LanguageCode code = LanguageCode.UNKNOWN;

	public LanguageCode getCode() {
		return code;
	}

	public void setCode(LanguageCode code) {
		this.code = code;
	}

	@JsonIgnore
	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<code>" + code + "</code>");
		return chunk.toString();
	}

}
