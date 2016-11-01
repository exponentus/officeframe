package reference.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.scripting._Session;

/**
 * 
 * 
 * @author Kayra created 07-01-2016
 */

@Entity
@Table(name = "positions", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "Position.findAll", query = "SELECT m FROM Position AS m ORDER BY m.regDate")
public class Position extends SimpleReferenceEntity {
	private int rank = 999;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		chunk.append("<rank>" + rank + "</rank>");
		return chunk.toString();
	}
}
