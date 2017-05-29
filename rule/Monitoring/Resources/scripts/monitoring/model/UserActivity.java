package monitoring.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.exponentus.dataengine.jpa.SimpleAppEntity;
import com.exponentus.runtimeobj.IAppEntity;
import com.exponentus.scripting.IPOJOObject;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonRootName;

import monitoring.model.constants.ActivityType;
import monitoring.model.constants.converter.ActivityTypeConverter;

@JsonRootName("userActivity")
@Entity
@Table(name = "monit__user_activities")
@NamedQuery(name = "UserActivity.findAll", query = "SELECT m FROM UserActivity AS m ORDER BY m.eventTime")
public class UserActivity extends SimpleAppEntity implements IPOJOObject {

	@Column(name = "act_user", nullable = false, updatable = false)
	private Long actUser;

	@Convert(converter = ActivityTypeConverter.class)
	private ActivityType type = ActivityType.UNKNOWN;

	@Column(name = "event_time", nullable = false)
	private Date eventTime;

	@Column(name = "related_to")
	private IAppEntity<UUID> relatedTo;

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public Long getActUser() {
		return actUser;
	}

	public void setActUser(Long actUser) {
		this.actUser = actUser;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public IAppEntity<UUID> getRelatedTo() {
		return relatedTo;
	}

	public void setRelatedTo(IAppEntity<UUID> relatedTo) {
		this.relatedTo = relatedTo;
	}

	@Override
	public String getEntityKind() {
		return this.getClass().getSimpleName().toLowerCase();
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
		chunk.append("<type>" + type + "</type>");
		return chunk.toString();
	}

}
