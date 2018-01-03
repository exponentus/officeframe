package calendar.model;

import calendar.init.ModuleConst;
import calendar.model.constants.ReminderType;
import calendar.model.constants.converter.ReminderTypeConverter;
import com.exponentus.common.model.SecureAppEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import staff.model.embedded.Observer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = ModuleConst.CODE + "__reminders")
public class Reminder extends SecureAppEntity<UUID> {

    private String description;

    @Convert(converter = ReminderTypeConverter.class)
    @Column(name = "type")
    private ReminderType reminderType = ReminderType.SILENT;

    @ElementCollection
    @CollectionTable(name = ModuleConst.CODE + "__reminder_observers", joinColumns = @JoinColumn(referencedColumnName = "id"))
    private List<Observer> observers = new ArrayList<Observer>();

    @ElementCollection
    @CollectionTable(name = ModuleConst.CODE + "__reminder_maillist", joinColumns = @JoinColumn(referencedColumnName = "id"))
    private List<Observer> mailList = new ArrayList<Observer>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReminderType getReminderType() {
        return reminderType;
    }

    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    public List<Observer> getMailList() {
        return mailList;
    }

    public void setMailList(List<Observer> mailList) {
        this.mailList = mailList;
    }

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "reminders/" + getId();
    }
}
