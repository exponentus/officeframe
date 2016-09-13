package monitoring.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import monitoring.model.constants.ActivityType;

@Entity
@Table(name = "_user_activities")
public class UserActivity {

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

}
