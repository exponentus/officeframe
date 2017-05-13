package discussing.model;

import java.util.ArrayList;
import java.util.List;

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
import com.exponentus.common.model.SecureHierarchicalEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.exponentus.env.Environment;
import com.exponentus.extconnect.IExtUser;
import com.exponentus.extconnect.IExtUserDAO;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("comment")
@Entity
@Table(name = "disc__comments")
@NamedQuery(name = "Comment.findAll", query = "SELECT m FROM Comment AS m ORDER BY m.regDate ASC")
public class Comment extends SecureHierarchicalEntity {

	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, nullable = false)
	private Topic topic;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Comment parentComment;

	@OneToMany(mappedBy = "parentComment")
	private List<Comment> subComments;

	@FTSearchable
	@Column(nullable = false, length = 512)
	private String comment;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "disc_comment_attachments", joinColumns = {
			@JoinColumn(name = "comment_id") }, inverseJoinColumns = {
					@JoinColumn(name = "attachment_id") }, indexes = {
							@Index(columnList = "comment_id, attachment_id") }, uniqueConstraints = @UniqueConstraint(columnNames = {
									"comment_id", "attachment_id" }))
	@CascadeOnDelete
	private List<Attachment> attachments = new ArrayList<>();

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
		IExtUser user = eDao.getEmployee(author.getId());
		if (user != null) {
			chunk.append("<author>" + user.getName() + "</author>");
		} else {
			chunk.append("<author>" + author + "</author>");
		}
		chunk.append("<comment>" + comment + "</comment>");
		return chunk.toString();
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		return getFullXMLChunk(ses);
	}

	@Override
	public SecureHierarchicalEntity getParentEntity(_Session ses) {
		if (topic != null) {
			return topic;
		} else {
			return parentComment;
		}
	}

}
