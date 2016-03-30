package reference.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import kz.lof.common.model.SimpleEntity;
import kz.lof.scripting._Session;
import reference.model.constants.KufType;

@Entity
@Table(name = "kufs")
@NamedQuery(name = "Kuf.findAll", query = "SELECT m FROM Kuf AS m ORDER BY m.regDate")
public class Kuf extends SimpleEntity {

	@Enumerated(EnumType.STRING)
	@Column(nullable = true, length = 32, unique = true)
	private KufType code = KufType.UNKNOWN;

	public KufType getKuf() {
		return code;
	}

	public void setKuf(KufType kuf) {
		this.code = kuf;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<kufcode>" + code + "</kufcode>");
		;
		return chunk.toString();
	}

}
