package discussing.dto;

import discussing.model.Comment;
import discussing.model.constants.TopicStatusType;
import reference.model.Tag;

import java.util.List;

public class TopicViewEntry {

    public TopicStatusType status = TopicStatusType.UNKNOWN;
    public String title;
    public String body;
    public List<Tag> tags;
    public List<Comment> comments;
    public boolean hasAttachments;
}
