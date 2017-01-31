package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import com.exponentus.common.model.SimpleReferenceEntity;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
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

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<name>" + getName().replace("&", "&amp;") + "</name>");
		chunk.append("<code>" + getCode() + "</code>");
		chunk.append("<localizednames>");
		try {
			LanguageDAO lDao = new LanguageDAO(ses);
			ViewPage<Language> list = lDao.findAll();
			for (Language l : list.getResult()) {
				chunk.append("<entry id=\"" + l.getCode() + "\">" + getLocName(l.getCode()).replace("&", "&amp;")
						+ "</entry>");
			}
		} catch (DAOException e) {
			Server.logger.errorLogEntry(e);
		}
		chunk.append("</localizednames>");
		return chunk.toString();
	}
	
}
