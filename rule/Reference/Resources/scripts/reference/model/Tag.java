package reference.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.util.UUIDConverter;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import administrator.dao.LanguageDAO;
import administrator.model.Language;

@JsonRootName("tag")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__tags", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "color" }))
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
@NamedQuery(name = "Tag.findAll", query = "SELECT m FROM Tag AS m WHERE m.parent IS NULL ORDER BY m.name")
public class Tag extends SimpleReferenceEntity {

	@Column(length = 7)
	private String color;

	@JsonIgnore
	@Convert("uuidConverter")
	@Basic(fetch = FetchType.LAZY, optional = true)
	private Tag parent;

	private boolean hidden;

	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Tag getParent() {
		return parent;
	}

	public void setParent(Tag parent) {
		this.parent = parent;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public UUID getParentId() {
		if (parent == null) {
			return null;
		}
		return parent.id;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void setParentId(UUID id) {
		if (id == null) {
			setParent(null);
			return;
		}

		Tag parent = new Tag();
		parent.setId(id);
		setParent(parent);
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<regdate>" + TimeUtil.dateTimeToStringSilently(regDate) + "</regdate>");
		chunk.append("<name>" + getName().replace("&", "&amp;") + "</name>");
		chunk.append("<color>" + color + "</color>");
		if (category != null) {
			chunk.append("<category>" + category + "</category>");
		}
		chunk.append("<hidden>" + hidden + "</hidden>");
		chunk.append("<localizednames>");
		try {
			LanguageDAO lDao = new LanguageDAO(ses);
			List<Language> list = lDao.findAllActivated();
			for (Language l : list) {
				chunk.append("<entry id=\"" + l.getCode() + "\">" + getLocName(l.getCode()) + "</entry>");
			}
		} catch (DAOException e) {
			Server.logger.exception(e);
		}

		chunk.append("</localizednames>");
		return chunk.toString();
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		return "<name>" + getName() + "</name><color>" + color + "</color><category>" + category + "</category>";
	}

	@Override
	public String toString() {
		return "Tag[" + id + ", " + getLocName() + ", " + color + ", " + parent + ", " + getRegDate() + "]";
	}
}
