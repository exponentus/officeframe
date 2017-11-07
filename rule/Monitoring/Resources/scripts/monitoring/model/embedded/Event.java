package monitoring.model.embedded;

import com.exponentus.dataengine.jpa.IAppEntity;
import com.fasterxml.jackson.annotation.JsonRootName;
import monitoring.model.constants.ActivityType;

import java.util.Date;
import java.util.UUID;

@JsonRootName("event")
public class Event {

    private Date time;

    private String description;

    private IAppEntity<UUID> prevState;

    private IAppEntity<UUID> afterState;

    private ActivityType type = ActivityType.UNKNOWN;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IAppEntity<UUID> getPrevState() {
        return prevState;
    }

    public void setPrevState(IAppEntity<UUID> prevState) {
        this.prevState = prevState;
    }

    public IAppEntity<UUID> getAfterState() {
        return afterState;
    }

    public void setAfterState(IAppEntity<UUID> afterState) {
        this.afterState = afterState;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

}
