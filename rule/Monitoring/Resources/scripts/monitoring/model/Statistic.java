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

@JsonRootName("statistic")
@Entity
@Table(name = "monit__statistics", uniqueConstraints = @UniqueConstraint(columnNames = { "app_code", "act_user", "event_time", "type",
		"status" }))
public class Statistic extends SimpleAppEntity {

	@Column(name = "act_user", nullable = false)
	private Long actUser;

	@Column(name = "app_code", nullable = false, length = 16)
	private String appCode;

	@Temporal(TemporalType.DATE)
	@Column(name = "event_time", nullable = false)
	private Date eventTime;

	@Column(length = 16)
	private String status;

	@Column(length = 64)
	private String type;

	long amount;

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

	public Long getActUser() {
		return actUser;
	}

	public void setActUser(Long actUser) {
		this.actUser = actUser;
	}

}
