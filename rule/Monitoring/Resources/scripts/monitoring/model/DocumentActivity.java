package monitoring.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import com.exponentus.dataengine.jpa.SimpleAppEntity;
import com.exponentus.dataengine.jpa.util.UUIDConverter;
import com.exponentus.scripting._Session;
import com.exponentus.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonRootName;

import monitoring.model.embedded.Event;
import monitoring.model.util.EventConverter;

@JsonRootName("documentActivity")
@Entity
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
@Table(name = "monit__doc_activities")
@NamedQuery(name = "DocumentActivity.findAll", query = "SELECT m FROM DocumentActivity AS m")
public class DocumentActivity extends SimpleAppEntity {

	private DocumentActivity parentActivity;

	@Column(name = "event_time", nullable = false)
	private Date eventTime;

	@Convert("uuidConverter")
	@Column(name = "act_entity_id", nullable = false)
	private UUID actEntityId;

	@Column(name = "act_entity_kind")
	private String actEntityKind;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "monit__events")
	@javax.persistence.Convert(converter = EventConverter.class)
	@Column(name = "event", columnDefinition = "jsonb")
	@OrderBy("sort")
	private List<Event> events = new ArrayList<Event>();

	public DocumentActivity getParentActivity() {
		return parentActivity;
	}

	public void setParentActivity(DocumentActivity parentActivity) {
		this.parentActivity = parentActivity;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
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

	public List<Event> getDetails() {
		return events;
	}

	public void setDetails(List<Event> details) {
		this.events = details;
	}

	public void addEvent(Event e) {
		events.add(e);
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
		return "p?id=documentactivity-form&amp;docid=" + getIdentifier();
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
