package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.exponentus.common.model.SimpleReferenceEntity;

@Entity
@Table(name = "document_type_category")
@NamedQuery(name = "DocumentTypeCategory.findAll", query = "SELECT m FROM DocumentTypeCategory AS m ORDER BY m.regDate")
public class DocumentTypeCategory extends SimpleReferenceEntity {
	String code;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
