package reference.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Cacheable(true)
@Table(name = "regions", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "country_id" }))
@NamedQuery(name = "Region.findAll", query = "SELECT m FROM Region AS m ORDER BY m.regDate")
public class Region extends SimpleReferenceEntity {
	
	@JsonIgnore
	@OneToMany(mappedBy = "region")
	private List<District> districts;
	
	// @JsonIgnore
	@ManyToOne
	@JoinColumn(nullable = false)
	private Country country;
	
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private RegionType type;
	
	public RegionType getType() {
		return type;
	}
	
	public void setType(RegionType type) {
		this.type = type;
	}
	
	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}
	
	public List<District> getDistricts() {
		return districts;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	public Country getCountry() {
		return country;
	}
	
	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		if (type != null && type.getId() != null) {
			chunk.append("<type id=\"" + type.getId() + "\">" + type.getLocalizedName(ses.getLang()) + "</type>");
		}
		if (country != null) {
			chunk.append("<country id=\"" + country.getId() + "\">" + country.getLocalizedName(ses.getLang())
					+ "</country>");
		}
		return chunk.toString();
	}
	
	// TODO consistency between Region and Country ??
	
	@Override
	public String getShortXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<name>" + getName() + "</name>");
		try {
			chunk.append("<type id=\"" + type.getId() + "\">" + type.getLocalizedName(ses.getLang()) + "</type>");
			if (country != null) {
				chunk.append("<country id=\"" + country.getId() + "\">" + country.getLocalizedName(ses.getLang())
						+ "</country>");
			}
		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
		}
		return chunk.toString();
	}
}
