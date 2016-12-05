package reference.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import administrator.dao.LanguageDAO;
import administrator.model.Language;

@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "color" }))
@NamedQuery(name = "Tag.findAll", query = "SELECT m FROM Tag AS m WHERE m.parent IS NULL ORDER BY m.name")
public class Tag extends SimpleReferenceEntity {
	
	@Column(length = 7)
	private String color;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	private Tag parent;
	
	@OneToMany(mappedBy = "parent", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<Tag> children;
	
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
	
	public List<Tag> getChildren() {
		return children;
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
			List<Language> list = lDao.findAll();
			for (Language l : list) {
				chunk.append("<entry id=\"" + l.getCode() + "\">" + getLocalizedName(l.getCode()) + "</entry>");
			}
		} catch (DAOException e) {
			Server.logger.errorLogEntry(e);
		}

		chunk.append("</localizednames>");
		return chunk.toString();
	}
	
	@Override
	public String getShortXMLChunk(_Session ses) {
		return "<name>" + getName() + "</name><color>" + color + "</color><category>"+category+"</category>";
	}
	
	@Override
	public String toString() {
		return "Tag[" + id + ", " + getLocalizedName() + ", " + color + ", " + parent + ", " + getAuthorId() + ", "
				+ getRegDate() + "]";
	}
}
