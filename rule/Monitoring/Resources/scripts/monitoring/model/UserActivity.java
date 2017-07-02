package monitoring.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Converter;

import com.exponentus.dataengine.jpa.SimpleAppEntity;
import com.exponentus.dataengine.jpa.util.UUIDConverter;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonRootName;

import monitoring.model.constants.ActivityType;

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
		return "p?id=useractivity-form&amp;docid=" + getIdentifier();
	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		return getShortXMLChunk(ses);
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<eventtime>" + TimeUtil.dateTimeToStringSilently(eventTime) + "</eventtime>");
		chunk.append("<actuser>" + actUser + "</actuser>");
		chunk.append("<ip>" + ip + "</ip>");
		chunk.append("<country>" + country + "</country>");
		chunk.append("<type>" + type.name() + "</type>");
		return chunk.toString();
	}

}
