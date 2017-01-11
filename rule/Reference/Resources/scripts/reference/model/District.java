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
import javax.validation.constraints.NotNull;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;

@Entity
@Cacheable(true)
@Table(name = "districts", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "region_id" }))
@NamedQuery(name = "District.findAll", query = "SELECT m FROM District AS m ORDER BY m.regDate")
public class District extends SimpleReferenceEntity {
	private List<Locality> localities;
	
	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private Region region;
	
	@OneToMany(mappedBy = "district")
	public List<Locality> getLocalities() {
		return localities;
	}
	
	public Region getRegion() {
		return region;
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<region id=\"" + region.getId() + "\">" + region.getLocalizedName(ses.getLang()) + "</region>");
		return chunk.toString();
	}
}
