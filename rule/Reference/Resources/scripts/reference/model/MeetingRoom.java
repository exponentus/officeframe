package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("meetingRoom")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__rooms", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQuery(name = "MeetingRoom.findAll", query = "SELECT m FROM MeetingRoom AS m ORDER BY m.regDate")
public class MeetingRoom extends SimpleReferenceEntity {
    private int floor = 1;
    private int capacity = 5;
}
