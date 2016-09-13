package monitoring.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.exponentus.common.model.Attachment;
import com.exponentus.scripting.IPOJOObject;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;

import monitoring.model.constants.ActivityType;

@Entity
@Table(name = "_user_activities")
@NamedQuery(name = "UserActivity.findAll", query = "SELECT m FROM UserActivity AS m ORDER BY m.eventTime")
public class UserActivity implements IPOJOObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	protected Long id;

	@Column(name = "act_user", nullable = false, updatable = false)
	private Long actUser;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 7)
	private ActivityType type = ActivityType.UNKNOWN;

	@Column(name = "event_time")
	private Date eventTime;

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
	public List<Attachment> getAttachments() {
		return null;
	}

	@Override
	public void setAttachments(List<Attachment> attachments) {

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

	@Override
	public boolean isWasRead() {
		return false;
	}

	@Override
	public Object getJSONObj(_Session ses) {
		return this;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

}
