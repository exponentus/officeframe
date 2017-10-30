package monitoring.model;

import com.exponentus.common.model.SimpleAppEntity;
import com.fasterxml.jackson.annotation.JsonRootName;
import monitoring.init.AppConst;

import javax.persistence.*;
import java.util.Date;

@JsonRootName("statistic")
@Entity
@Table(name = "monit__statistics", uniqueConstraints = @UniqueConstraint(columnNames = {"app_code", "act_user", "event_time", "type",
        "status"}))
public class Statistic extends SimpleAppEntity {

    long amount;
    @Column(name = "act_user", nullable = false)
    private Long actUser;
    @Column(name = "app_code", nullable = false, length = 16)
    private String appCode;
    @Temporal(TemporalType.DATE)
    @Column(name = "event_time", nullable = false)
    private Date eventTime;
    @Column(length = 16)
    private String status;
    //0889803556  стоян
    @Column(length = 64)
    private String type;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getIdentifier() {
        return id.toString();
    }

    public Long getActUser() {
        return actUser;
    }

    public void setActUser(Long actUser) {
        this.actUser = actUser;
    }

    public String getURL() {
        return AppConst.BASE_URL + "statistic/" + getIdentifier();
    }
}
