package discussing.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.dataengine.jpa.SecureAppEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

import discussing.model.constants.TopicStatusType;

@Entity
@Table(name = "topics", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "Topic.findAll", query = "SELECT m FROM Topic AS m ORDER BY m.regDate")
@JsonIgnoreType
public class Topic extends SecureAppEntity<UUID> {

	private String subject;

	private String module;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 3)
	private TopicStatusType status = TopicStatusType.UNKNOWN;

	@OneToMany(mappedBy = "topic")
	private List<Comment> comments;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

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

}
