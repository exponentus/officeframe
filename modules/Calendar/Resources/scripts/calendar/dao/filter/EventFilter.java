package calendar.dao.filter;

import calendar.model.Event;
import com.exponentus.common.model.constants.PriorityType;
import com.exponentus.dataengine.IFilter;
import reference.model.Tag;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class EventFilter implements IFilter<Event> {

    private Date eventStart;
    private Date eventEnd;
    private PriorityType priority = PriorityType.UNKNOWN;
    private List<Tag> tags;

    public Date getEventStart() {
        return eventStart;
    }

    public void setEventStart(Date eventStart) {
        this.eventStart = eventStart;
    }

    public Date getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(Date eventEnd) {
        this.eventEnd = eventEnd;
    }

    public PriorityType getPriority() {
        return priority;
    }

    public void setPriority(PriorityType priority) {
        this.priority = priority;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Predicate collectPredicate(Root<Event> root, CriteriaBuilder cb, Predicate condition) {

        if (eventStart != null) {
            if (condition == null) {
                condition = cb.and(cb.greaterThanOrEqualTo(root.get("eventTime"), eventStart));
            } else {
                condition = cb.and(cb.greaterThanOrEqualTo(root.get("eventTime"), eventStart), condition);
            }
        }

        if (eventEnd != null) {
            if (condition == null) {
                condition = cb.and(cb.lessThanOrEqualTo(root.get("eventTime"), eventEnd));
            } else {
                condition = cb.and(cb.lessThanOrEqualTo(root.get("eventTime"), eventEnd), condition);
            }
        }

        return condition;
    }
}
