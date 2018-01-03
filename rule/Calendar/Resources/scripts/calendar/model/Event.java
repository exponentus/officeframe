package calendar.model;

import calendar.init.ModuleConst;
import com.exponentus.common.model.SecureAppEntity;
import com.exponentus.common.model.constants.PriorityType;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.model.Tag;
import staff.model.embedded.Observer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = ModuleConst.CODE + "__events")
public class Event extends SecureAppEntity<UUID> {

    @Column(name = "event_time")
    private Date eventTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private PriorityType priority = PriorityType.NORMAL;

    private Reminder reminder;

    private String description;

    @ElementCollection
    @CollectionTable(name = ModuleConst.CODE + "__event_observers", joinColumns = @JoinColumn(referencedColumnName = "id"))
    private List<Observer> observers = new ArrayList<Observer>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = ModuleConst.CODE + "__event_tags")
    private List<Tag> tags;

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public PriorityType getPriority() {
        return priority;
    }

    public void setPriority(PriorityType priority) {
        this.priority = priority;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "events/" + getId();
    }
}
