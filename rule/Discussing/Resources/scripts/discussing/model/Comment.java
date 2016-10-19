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

import org.eclipse.persistence.annotations.CascadeOnDelete;

import com.exponentus.common.model.Attachment;
import com.exponentus.dataengine.jpa.SecureAppEntity;
import com.exponentus.dataengine.system.IEmployee;
import com.exponentus.dataengine.system.IExtUserDAO;
import com.exponentus.env.Environment;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
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

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Comment parent;

	@OneToMany(mappedBy = "parent")
	private List<Comment> subComments;

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

	public List<Comment> getSubComments() {
		return subComments;
	}

	public void setSubComments(List<Comment> subComments) {
		this.subComments = subComments;
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
		if (isHasSubComments()) {
			chunk.append("<hascomments>true</hascomments>");
			chunk.append("<comments>");
			for (Comment aCommentsEntry : getSubComments()) {
				chunk.append(aCommentsEntry.getShortXMLChunkAsEntry(ses));
			}
			chunk.append("</comments>");
		} else {
			chunk.append("<hascomments>false</hascomments>");
		}

		return chunk.toString();
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		return getFullXMLChunk(ses);
	}

	public boolean isHasSubComments() {
		return subComments != null && subComments.size() > 0;
	}

}
