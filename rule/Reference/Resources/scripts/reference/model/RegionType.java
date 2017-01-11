package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.exponentus.common.model.SimpleReferenceEntity;

import reference.model.constants.RegionCode;

@Entity
@Cacheable(true)
@Table(name = "region_types")
@NamedQuery(name = "RegionType.findAll", query = "SELECT m FROM RegionType AS m ORDER BY m.regDate")
public class RegionType extends SimpleReferenceEntity {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = true, length = 32, unique = true)
	private RegionCode code = RegionCode.UNKNOWN;
	
	public RegionCode getCode() {
		return code;
	}
	
	public void setCode(RegionCode code) {
		this.code = code;
	}
	
}
