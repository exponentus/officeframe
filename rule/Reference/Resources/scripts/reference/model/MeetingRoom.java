package reference.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("meetingRoom")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "meeting_rooms", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQuery(name = "MeetingRoom.findAll", query = "SELECT m FROM MeetingRoom AS m ORDER BY m.regDate")
public class MeetingRoom extends SimpleReferenceEntity {

}
