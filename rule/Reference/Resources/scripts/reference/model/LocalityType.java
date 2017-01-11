package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.exponentus.common.model.SimpleReferenceEntity;

import reference.model.constants.LocalityCode;

@Entity
@Cacheable(true)
@Table(name = "locality_types")
@NamedQuery(name = "LocalityType.findAll", query = "SELECT m FROM LocalityType AS m ORDER BY m.regDate")
public class LocalityType extends SimpleReferenceEntity {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = true, length = 32, unique = true)
	private LocalityCode code = LocalityCode.UNKNOWN;
	
	public LocalityCode getCode() {
		return code;
	}
	
	public void setCode(LocalityCode code) {
		this.code = code;
	}
	
}
