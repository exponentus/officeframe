package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;

@Entity
@Table(name = "task_type", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }) )
@NamedQuery(name = "TaskType.findAll", query = "SELECT m FROM TaskType AS m ORDER BY m.regDate")
public class TaskType extends SimpleReferenceEntity {
	public String prefix;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		if(this.prefix != null) {
			chunk.append("<prefix>" + this.prefix + "</prefix>");
		}
		return chunk.toString();
	}
}
