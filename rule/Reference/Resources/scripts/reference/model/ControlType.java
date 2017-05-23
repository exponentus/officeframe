package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import reference.model.constants.ControlSchemaType;
import reference.model.constants.converter.ControlSchemaTypeConverter;

@JsonRootName("controlType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__control_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "ControlType.findAll", query = "SELECT m FROM ControlType AS m ORDER BY m.regDate")
public class ControlType extends SimpleReferenceEntity {

	@Column(length = 16, unique = true)
	private String code;

	@Convert(converter = ControlSchemaTypeConverter.class)
	private ControlSchemaType schema;

	private String category;

	@Column(name = "default_hours")
	private int defaultHours;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ControlSchemaType getSchema() {
		return schema;
	}

	public void setSchema(ControlSchemaType schema) {
		this.schema = schema;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getDefaultHours() {
		return defaultHours;
	}

	public void setDefaultHours(int defaultHours) {
		this.defaultHours = defaultHours;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<defaulthours>" + defaultHours + "</defaulthours>");
		chunk.append("<schema>" + schema + "</schema>");
		return chunk.toString();
	}
}
