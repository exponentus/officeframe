package reference.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.rest.outgoingpojo.IPayload;
import com.exponentus.scripting._Session;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import reference.model.constants.CountryCode;

@Entity
@Table(name = "countries", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "code" }))
@NamedQuery(name = "Country.findAll", query = "SELECT m FROM Country AS m ORDER BY m.regDate")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonRootName("country")
@JsonIgnoreType
public class Country extends SimpleReferenceEntity {
	@JsonIgnore
	@OneToMany(mappedBy = "country")
	private List<Region> regions;

	@Enumerated(EnumType.STRING)
	@Column(nullable = true, length = 7, unique = true)
	private CountryCode code = CountryCode.UNKNOWN;

	public CountryCode getCode() {
		return code;
	}

	public void setCode(CountryCode code) {
		this.code = code;
	}

	@JsonIgnore
	public List<Region> getRegions() {
		return regions;
	}

	@JsonIgnore
	public void setRegions(List<Region> regions) {
		this.regions = regions;
	}

	@JsonIgnore
	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<code>" + code + "</code>");
		return chunk.toString();
	}

	@JsonIgnore
	public IPayload getMock() {
		return new Country();
	}

}
