package monitoring.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.exponentus.dataengine.jpa.SimpleAppEntity;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonRootName;

import administrator.model.User;

@JsonRootName("statistic")
@Entity
@Table(name = "monit__statistics", uniqueConstraints = @UniqueConstraint(columnNames = { "app_code", "user", "event_time", "type" }))
public class Statistic extends SimpleAppEntity {

	private User user;

	@Column(name = "app_code", nullable = false, length = 32)
	private String appCode;

	@Temporal(TemporalType.DATE)
	@Column(name = "event_time", nullable = false)
	private Date eventTime;

	@Column(length = 140)
	private String type;

	long amount;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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

	@Override
	public String getIdentifier() {
		return id.toString();
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		return getShortXMLChunk(ses);
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<eventtime>" + TimeUtil.dateTimeToStringSilently(eventTime) + "</eventtime>");
		return chunk.toString();
	}

}
