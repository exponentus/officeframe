package staff.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import java.util.List;

@JsonRootName("individual")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "staff__individuals", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "bin" }))
public class Individual extends SimpleReferenceEntity {

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "staff__individual_labels", joinColumns = @JoinColumn(name = "individual_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
	private List<IndividualLabel> labels;

	@FTSearchable
	@Column(length = 12)
	private String bin = "";

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}



	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<bin>" + bin + "</bin>");
		chunk.append("<labels>");
		for (IndividualLabel l : labels) {
			chunk.append("<entry id=\"" + l.getId() + "\">" + l.getLocName(ses.getLang()) + "</entry>");
		}
		chunk.append("</labels>");
		return chunk.toString();
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<name>" + getName().replaceAll("&", "&amp;") + "</name>");
		chunk.append("<bin>" + bin + "</bin>");
		chunk.append("<labels>" + labels + "</labels>");
		return chunk.toString();
	}
}
