package monitoring.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import com.exponentus.common.model.embedded.CrossLink;
import com.exponentus.common.model.util.CrossLinkConverter;
import com.exponentus.dataengine.jpa.SimpleAppEntity;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonRootName;

import monitoring.model.constants.ActivityType;
import monitoring.model.constants.converter.ActivityTypeConverter;

@JsonRootName("userActivity")
@Entity
@Table(name = "monit__user_activities")
@Converter(name = "cl_conv", converterClass = CrossLinkConverter.class)
public class UserActivity extends SimpleAppEntity {

	@Column(name = "act_user", nullable = false, updatable = false)
	private Long actUser;

	@javax.persistence.Convert(converter = ActivityTypeConverter.class)
	private ActivityType type = ActivityType.UNKNOWN;

	@Column(name = "event_time", nullable = false)
	private Date eventTime;

	@Convert("cl_conv")
	@Basic
	private CrossLink relatedTo;

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

	public CrossLink getRelatedTo() {
		return relatedTo;
	}

	public void setRelatedTo(CrossLink relatedTo) {
		this.relatedTo = relatedTo;
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
