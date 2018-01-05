package discussing.dao.filter;

import administrator.model.User;
import com.exponentus.runtimeobj.Filter;
import discussing.model.constants.TopicStatusType;
import reference.model.Tag;

import java.util.List;

public class TopicFilter extends Filter {

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
}
