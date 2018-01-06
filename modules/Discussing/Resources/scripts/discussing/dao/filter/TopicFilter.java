package discussing.dao.filter;

import administrator.model.User;
import com.exponentus.dataengine.IFilter;
import discussing.model.Topic;
import discussing.model.constants.TopicStatusType;
import reference.model.Tag;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class TopicFilter implements IFilter<Topic> {

    private TopicStatusType status = TopicStatusType.UNKNOWN;
    private List<Tag> tags;
    private User author;

    public TopicStatusType getStatus() {
        return status;
    }

    public void setStatus(TopicStatusType status) {
        this.status = status;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public Predicate collectPredicate(Root<Topic> root, CriteriaBuilder cb, Predicate condition) {
        if (author != null) {
            if (condition == null) {
                condition = cb.equal(root.get("author"), author);
            } else {
                condition = cb.and(cb.equal(root.get("author"), author), condition);
            }
        }

        if (status != TopicStatusType.UNKNOWN) {
            if (condition == null) {
                condition = cb.equal(root.get("status"), status);
            } else {
                condition = cb.and(cb.equal(root.get("status"), status), condition);
            }
        }

        if (tags != null) {
            if (condition == null) {
                condition = root.get("tags").in(tags);
            } else {
                condition = cb.and(root.get("tags").in(tags), condition);
            }
        }

        return condition;
    }
}
