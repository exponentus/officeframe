package monitoring.model;

import administrator.model.User;
import com.exponentus.common.model.SimpleAppEntity;
import com.exponentus.common.model.converter.UUIDConverter;
import com.exponentus.common.model.converter.UserConverter;
import com.fasterxml.jackson.annotation.JsonRootName;
import monitoring.init.AppConst;
import monitoring.model.constants.ActivityType;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Converters;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@JsonRootName("userActivity")
@Entity
@Table(name = AppConst.CODE + "__user_activities")
@Converters({@Converter(name = "user_conv", converterClass = UserConverter.class),
        @Converter(name = "uuidConverter", converterClass = UUIDConverter.class)})
public class UserActivity extends SimpleAppEntity {

    @Convert("user_conv")
    @Basic(optional = true)
    @Column(name = "act_user", nullable = false)
    private User actUser;

    @Column(name = "event_time", nullable = false)
    private Date eventTime;

    private ActivityType type = ActivityType.UNKNOWN;

    private String ip = "";

    private String country = "";

    @Convert("uuidConverter")
    @Column(name = "act_entity_id", nullable = false)
    private UUID actEntityId;

    @Column(name = "act_entity_kind")
    private String actEntityKind;

    public String getKind() {
        return "userActivity";
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public User getActUser() {
        return actUser;
    }

    public void setActUser(User actUser) {
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

    public UUID getActEntityId() {
        return actEntityId;
    }

    public void setActEntityId(UUID actEntityId) {
        this.actEntityId = actEntityId;
    }

    public String getActEntityKind() {
        return actEntityKind;
    }

    public void setActEntityKind(String actEntityKind) {
        this.actEntityKind = actEntityKind;
    }

    @Override
    public String getIdentifier() {
        return id.toString();
    }

    @Override
    public String getURL() {
        return null; // AppConst.BASE_URL + "user-activities/" + getIdentifier();
    }
}
