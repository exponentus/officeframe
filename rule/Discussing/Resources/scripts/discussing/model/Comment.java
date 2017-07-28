package discussing.model;

import com.exponentus.common.model.Attachment;
import com.exponentus.common.model.SecureAppEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import discussing.init.AppConst;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonRootName("comment")
@Entity
@Table(name = "disc__comments")
public class Comment extends SecureAppEntity<UUID> {

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false, nullable = false)
    private Topic topic;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("regDate ASC")
    private List<Comment> comments;

    @FTSearchable
    @Column(nullable = false, length = 512)
    private String comment;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "disc_comment_attachments",
            joinColumns = {@JoinColumn(name = "comment_id")},
            inverseJoinColumns = {@JoinColumn(name = "attachment_id")},
            indexes = {@Index(columnList = "comment_id, attachment_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "attachment_id"}))
    @CascadeOnDelete
    private List<Attachment> attachments = new ArrayList<>();

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setTask(Topic t) {
        this.topic = t;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> subComments) {
        this.comments = subComments;
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
    public String getURL() {
        return AppConst.BASE_URL + "comments/" + getIdentifier();
    }
}
