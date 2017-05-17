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

@JsonRootName("organizationLabel")
@Entity
@Table(name = "staff__org_labels", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "OrganizationLabel.findAll", query = "SELECT m FROM OrganizationLabel AS m ORDER BY m.regDate")
public class OrganizationLabel extends SimpleReferenceEntity {

	@ManyToMany(mappedBy = "labels")
	private List<Organization> labels;

	@Convert(converter = LocalizedValConverter.class)
	@Column(name = "localized_descr", columnDefinition = "json")
	private Map<LanguageCode, String> localizedDescr;

	@JsonIgnore
	public List<Organization> getLabels() {
		return labels;
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
		return "<name>" + getLocName(ses.getLang()) + "</name>";
	}

	@Override
	public String getURL() {
		return "p?id=organization-label-form&amp;docid=" + getId();
	}
}
