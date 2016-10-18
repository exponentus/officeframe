package discussing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.exponentus.dataengine.system.IEmployee;
import com.exponentus.dataengine.system.IExtUserDAO;
import com.exponentus.env.Environment;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import com.exponentus.common.model.Attachment;
import com.exponentus.dataengine.jpa.SecureAppEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("comment")
@Entity
@Table(name = "comments")
@NamedQuery(name = "Comment.findAll", query = "SELECT m FROM Comment AS m ORDER BY m.regDate ASC")
public class Comment extends SecureAppEntity<UUID> {

	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, nullable = false)
	private Topic topic;

	@Column(nullable = false, length = 512)
	private String comment;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "comment_attachments", joinColumns = { @JoinColumn(name = "comment_id") }, inverseJoinColumns = {
	        @JoinColumn(name = "attachment_id") }, indexes = {
	                @Index(columnList = "comment_id, attachment_id") }, uniqueConstraints = @UniqueConstraint(columnNames = { "comment_id",
	                        "attachment_id" }))
	@CascadeOnDelete
	private List<Attachment> attachments = new ArrayList<>();

	@Override
	public long getAuthorId() {
		return author;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTask(Topic t) {
		this.topic = t;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public List<Attachment> getAttachments() {
		return attachments;
	}

	@Override
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<regdate>" + TimeUtil.dateTimeToStringSilently(regDate) + "</regdate>");
		IExtUserDAO eDao = Environment.getExtUserDAO();
		IEmployee user = eDao.getEmployee(author);
		if (user != null) {
			chunk.append("<author>" + user.getName() + "</author>");
		} else {
			chunk.append("<author>" + author + "</author>");
		}
		chunk.append("<comment>" + comment + "</comment>");
		chunk.append("<topic id='"+ topic.getId() +"'>" + topic.getSubject() + "</topic>");

		return chunk.toString();
	}
}
