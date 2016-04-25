package reference.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.exponentus.common.model.SimpleEntity;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "localities", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "type_id", "region_id" }) )
@NamedQuery(name = "Locality.findAll", query = "SELECT m FROM Locality AS m ORDER BY m.regDate")
public class Locality extends SimpleEntity {

	@JsonIgnore
	@OneToMany(mappedBy = "locality")
	private List<Street> streets;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private LocalityType type;

	@JsonIgnore
	@NotNull
	@ManyToOne(optional = true)
	@JoinColumn(nullable = false)
	private District district;

	@JsonIgnore
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

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<localitytype id=\"" + type.getId() + "\">" + type.getLocalizedName(ses.getLang()) + "</localitytype>");
		chunk.append("<region id=\"" + region.getId() + "\">" + region.getLocalizedName(ses.getLang()) + "</region>");
		if (district != null) {
			chunk.append("<district id=\"" + district.getId() + "\">" + district.getLocalizedName(ses.getLang()) + "</district>");
		}
		return chunk.toString();
	}
}
