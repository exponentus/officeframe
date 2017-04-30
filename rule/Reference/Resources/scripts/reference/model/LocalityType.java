package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.ViewPage;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import reference.model.constants.LocalityCode;

@JsonRootName("localityType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__locality_types", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "code" }))
@NamedQuery(name = "LocalityType.findAll", query = "SELECT m FROM LocalityType AS m ORDER BY m.regDate")
public class LocalityType extends SimpleReferenceEntity {

	@Enumerated(EnumType.STRING)
	@Column(nullable = true, length = 32)
	private LocalityCode code = LocalityCode.UNKNOWN;

	public LocalityCode getCode() {
		return code;
	}

	public void setCode(LocalityCode code) {
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
