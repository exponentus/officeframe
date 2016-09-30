package reference.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;

@Entity
@Table(name = "control_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "ControlType.findAll", query = "SELECT m FROM ControlType AS m ORDER BY m.regDate")
public class ControlType extends SimpleReferenceEntity {

	@Column(name = "default_hours")
	private int defautltHours;

	public int getDefautltHours() {
		return defautltHours;
	}

	public void setDefautltHours(int defautltHours) {
		this.defautltHours = defautltHours;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<defaulthours>" + defautltHours + "</defaulthours>");
		return chunk.toString();
	}
}
