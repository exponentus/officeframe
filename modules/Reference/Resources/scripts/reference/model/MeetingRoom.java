package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__rooms", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQuery(name = "MeetingRoom.findAll", query = "SELECT m FROM MeetingRoom AS m ORDER BY m.regDate")
public class MeetingRoom extends SimpleReferenceEntity {
    private int floor = 1;
    private int capacity = 5;

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "meeting-rooms/" + getIdentifier();
    }
}
