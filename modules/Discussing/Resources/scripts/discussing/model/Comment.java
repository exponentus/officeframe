package discussing.model;

import com.exponentus.common.model.Attachment;
import com.exponentus.common.model.SecureAppEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import discussing.init.ModuleConst;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = ModuleConst.CODE + "__comments")
public class Comment extends SecureAppEntity<UUID> {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false, nullable = false)
    private Topic topic;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("regDate DESC")
    private List<Comment> comments;

    @FTSearchable
    @Column(nullable = false, length = 512)
    private String comment;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = ModuleConst.CODE + "__comment_attachments",
            joinColumns = {@JoinColumn(name = "comment_id")},
            inverseJoinColumns = {@JoinColumn(name = "attachment_id")},
            indexes = {@Index(columnList = "comment_id, attachment_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "attachment_id"}))
    @CascadeOnDelete
    private List<Attachment> attachments = new ArrayList<>();

    @JsonIgnore
    public Topic getTopic() {
        return topic;
    }

    @JsonProperty
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @JsonIgnore
    public Comment getParent() {
        return parent;
    }

    @JsonProperty
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
        return ModuleConst.BASE_URL + "comments/" + getId();
    }
}
