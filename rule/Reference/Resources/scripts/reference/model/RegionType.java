package reference.model;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import reference.model.constants.RegionCode;

import javax.persistence.*;

@JsonRootName("regionType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__region_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "code" }))
@NamedQuery(name = "RegionType.findAll", query = "SELECT m FROM RegionType AS m ORDER BY m.regDate")
public class RegionType extends SimpleReferenceEntity {

	@Enumerated(EnumType.STRING)
	@Column(nullable = true, length = 32)
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
			Server.logger.exception(e);
		}
		chunk.append("</localizednames>");
		return chunk.toString();
	}
}
