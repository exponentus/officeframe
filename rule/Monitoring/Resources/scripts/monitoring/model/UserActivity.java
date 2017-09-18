package monitoring.model;

import com.exponentus.common.model.SimpleAppEntity;
import com.fasterxml.jackson.annotation.JsonRootName;
import monitoring.init.AppConst;
import monitoring.model.constants.ActivityType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;

@JsonRootName("userActivity")
@Entity
@Table(name = "monit__user_activities")
@NamedQuery(name = "UserActivity.findAll", query = "SELECT m FROM UserActivity AS m")
public class UserActivity extends SimpleAppEntity {

    @Column(name = "act_user", nullable = false)
    private Long actUser;

    @Column(name = "event_time", nullable = false)
    private Date eventTime;

    private ActivityType type = ActivityType.UNKNOWN;

    private String ip = "";

    private String country = "";

    public String getKind() {
        return "userActivity";
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Long getActUser() {
        return actUser;
    }

    public void setActUser(Long actUser) {
        this.actUser = actUser;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String getIdentifier() {
        return id.toString();
    }

    @Override
    public String getURL() {
        return AppConst.BASE_URL + "user-activities/" + getIdentifier();
    }
}
