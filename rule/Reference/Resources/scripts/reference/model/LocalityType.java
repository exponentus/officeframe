package reference.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import kz.lof.common.model.SimpleEntity;
import reference.model.constants.LocalityCode;

@Entity
@Table(name = "locality_types")
@NamedQuery(name = "LocalityType.findAll", query = "SELECT m FROM LocalityType AS m ORDER BY m.regDate")
public class LocalityType extends SimpleEntity {

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
