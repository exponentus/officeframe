package calendar.model;

import calendar.init.AppConst;
import calendar.model.constants.ReminderType;
import calendar.model.constants.converter.ReminderTypeConverter;
import com.exponentus.common.model.SecureAppEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import staff.model.embedded.Observer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonRootName("reminder")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = AppConst.CODE + "__reminders")
public class Reminder extends SecureAppEntity<UUID> {

    private String description;

    @Convert(converter = ReminderTypeConverter.class)
    @Column(name = "type")
    private ReminderType reminderType = ReminderType.SILENT;

    @ElementCollection
    @CollectionTable(name = AppConst.CODE + "__reminder_observers", joinColumns = @JoinColumn(referencedColumnName = "id"))
    private List<Observer> observers = new ArrayList<Observer>();

    @ElementCollection
    @CollectionTable(name = AppConst.CODE + "__reminder_maillist", joinColumns = @JoinColumn(referencedColumnName = "id"))
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
        return AppConst.BASE_URL + "reminders/" + getId();
    }

}
