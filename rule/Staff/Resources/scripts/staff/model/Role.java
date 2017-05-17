package staff.model;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.common.model.util.LocalizedValConverter;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import administrator.dao.LanguageDAO;
import administrator.model.Language;

@JsonRootName("role")
@Entity
@Table(name = "staff__roles", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "Role.findAll", query = "SELECT m FROM Role AS m ORDER BY m.regDate")
public class Role extends SimpleReferenceEntity {

	@ManyToMany(mappedBy = "roles")
	private List<Employee> employees;

	@Convert(converter = LocalizedValConverter.class)
	@Column(name = "localized_descr", columnDefinition = "json")
	private Map<LanguageCode, String> localizedDescr;

	@JsonIgnore
	public List<Employee> getEmployees() {
		return employees;
	}

	public Map<LanguageCode, String> getLocalizedDescr() {
		return localizedDescr;
	}

	public void setLocalizedDescr(Map<LanguageCode, String> localizedDescr) {
		this.localizedDescr = localizedDescr;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append(super.getFullXMLChunk(ses));
		List<Language> list = null;
		try {
			LanguageDAO lDao = new LanguageDAO(ses);
			list = lDao.findAllActivated();
		} catch (DAOException e) {
			Server.logger.exception(e);
		}
		chunk.append("<localizeddescr>");
		if (list != null) {
			for (Language l : list) {
				try {
					chunk.append("<entry id=\"" + l.getCode() + "\">" + localizedDescr.get(l.getCode()) + "</entry>");
				} catch (NullPointerException e) {
					chunk.append("<entry id=\"" + l.getCode() + "\"></entry>");
				}
			}
		}
		chunk.append("</localizeddescr>");
		return chunk.toString();
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<name>" + getLocName(ses.getLang()) + "</name>");
		return chunk.toString();
	}
}
