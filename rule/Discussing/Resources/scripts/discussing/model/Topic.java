package discussing.model;

import com.exponentus.common.model.Attachment;
import com.exponentus.common.model.SecureAppEntity;
import com.exponentus.dataengine.jpadatabase.ftengine.FTSearchable;
import com.fasterxml.jackson.annotation.JsonRootName;
import discussing.init.AppConst;
import discussing.model.constants.TopicStatusType;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import reference.model.Tag;
import staff.model.embedded.Observer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonRootName("topic")
@Entity
@Table(name = "disc__topics")
public class Topic extends SecureAppEntity<UUID> {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 7)
    private TopicStatusType status = TopicStatusType.UNKNOWN;

    @FTSearchable
    @Column(columnDefinition = "TEXT")
    private String body;

    @OneToMany(mappedBy = "topic")
    @OrderBy("regDate ASC")
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "disc_topic_tags")
    private List<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "disc__topic_attachments",
            joinColumns = {@JoinColumn(name = "topic_id")},
            inverseJoinColumns = {@JoinColumn(name = "attachment_id")},
            indexes = {@Index(columnList = "topic_id, attachment_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"topic_id", "attachment_id"}))
    @CascadeOnDelete
    private List<Attachment> attachments = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "disc__topic_observers", joinColumns = @JoinColumn(referencedColumnName = "id"))
    private List<Observer> observers = new ArrayList<Observer>();

    public TopicStatusType getStatus() {
        return status;
    }

    public void setStatus(TopicStatusType s) {
        this.status = s;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public List<Attachment> getAttachments() {
        return attachments;
    }

    @Override
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "topics/" + getIdentifier();
    }
}
