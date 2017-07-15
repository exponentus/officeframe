package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.converter.UUIDConverter;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.eclipse.persistence.annotations.Converter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonRootName("locality")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__localities", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "type_id", "region_id" }))
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
public class Locality extends SimpleReferenceEntity {

	@JsonIgnore
	@OneToMany(mappedBy = "locality")
	private List<Street> streets;

	@JsonIgnore
	@OneToMany(mappedBy = "locality")
	private List<CityDistrict> districts;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private LocalityType type;

	// @JsonIgnore
	@NotNull
	@ManyToOne(optional = true)
	@JoinColumn(nullable = false)
	private Region region;

	public LocalityType getType() {
		return type;
	}

	public void setType(LocalityType type) {
		this.type = type;
	}

	public List<Street> getStreets() {
		return streets;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setStreets(List<Street> streets) {
		this.streets = streets;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<localitytype id=\"" + type.getId() + "\">" + type.getLocName(ses.getLang()) + "</localitytype>");
		chunk.append("<region id=\"" + region.getId() + "\">" + region.getLocName(ses.getLang()) + "</region>");

		return chunk.toString();
	}
}
