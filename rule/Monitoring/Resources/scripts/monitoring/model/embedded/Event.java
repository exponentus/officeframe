package monitoring.model.embedded;

import com.fasterxml.jackson.annotation.JsonRootName;
import monitoring.model.constants.ActivityType;

import java.util.Date;

@JsonRootName("event")
public class Event {
    private Date time;
    private String addInfo;
    private ActivityType type = ActivityType.UNKNOWN;

    private String locInfo;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getLocInfo() {
        return locInfo;
    }

    public void setLocInfo(String locInfo) {
        this.locInfo = locInfo;
    }
}
