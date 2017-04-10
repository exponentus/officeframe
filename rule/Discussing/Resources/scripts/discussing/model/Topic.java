package discussing.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SecureHierarchicalEntity;
import com.exponentus.dataengine.system.IEmployee;
import com.exponentus.dataengine.system.IExtUserDAO;
import com.exponentus.env.Environment;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

import discussing.model.constants.TopicStatusType;

@Entity
@Table(name = "topics", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "Topic.findAll", query = "SELECT m FROM Topic AS m ORDER BY m.regDate")
@JsonIgnoreType
public class Topic extends SecureHierarchicalEntity {

	@Column(length = 64)
	private String module;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 7)
	private TopicStatusType status = TopicStatusType.UNKNOWN;

	@OneToMany(mappedBy = "topic")
	private List<Comment> comments;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public TopicStatusType getStatus() {
		return status;
	}

	public void setStatus(TopicStatusType s) {
		this.status = s;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<regdate>" + TimeUtil.dateTimeToStringSilently(regDate) + "</regdate>");
		IExtUserDAO eDao = Environment.getExtUserDAO();
		IEmployee user = eDao.getEmployee(author.getId());
		if (user != null) {
			chunk.append("<author>" + user.getName() + "</author>");
		} else {
			chunk.append("<author>" + author + "</author>");
		}
		chunk.append("<status>" + status + "</status>");
		return chunk.toString();
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<regdate>" + TimeUtil.dateTimeToStringSilently(regDate) + "</regdate>");
		chunk.append("<status>" + status + "</status>");
		return chunk.toString();
	}

}
