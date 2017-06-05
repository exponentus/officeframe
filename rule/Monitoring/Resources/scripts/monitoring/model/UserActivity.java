package monitoring.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Converter;

import com.exponentus.dataengine.jpa.SimpleAppEntity;
import com.exponentus.dataengine.jpa.util.UUIDConverter;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("userActivity")
@Entity
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
@Table(name = "monit__user_activities")
public class UserActivity extends SimpleAppEntity {

	@Column(name = "act_user", nullable = false)
	private Long actUser;

	@Column(name = "event_time", nullable = false)
	private Date eventTime;

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
		return chunk.toString();
	}

}
